# TravelingApp - Android

Aplicacion nativa Android para la gestion de viajes: busqueda y reserva de hoteles, alquiler de coches, gestion de transportes y catalogo de destinos. Soporta sesion con roles (cliente / negocio) y modo offline con sincronizacion en background.

## Stack tecnologico

| Tecnologia | Version | Uso |
|---|---|---|
| Kotlin | 2.2.10 | Lenguaje principal (100%) |
| Jetpack Compose (BOM) | 2026.02.00 | UI declarativa |
| Material 3 | via BOM | Sistema de diseño (envuelto en wrappers Travel/App) |
| Navigation Compose | 2.9.7 | Navegacion entre pantallas con rutas tipadas (kotlinx-serialization) |
| Hilt | 2.59.2 | Inyeccion de dependencias |
| Hilt Work | 1.2.0 | Workers inyectables con Hilt |
| KSP | 2.3.2 | Procesador de anotaciones (Hilt, Room) |
| Room | 2.8.4 | Cache offline y outbox de operaciones pendientes |
| WorkManager | 2.10.1 | Sincronizacion offline -> online en background |
| Retrofit | 3.0.0 | Cliente HTTP contra la API y mocks |
| Gson Converter | 3.0.0 | Serializacion JSON para Retrofit |
| OkHttp MockWebServer | 4.12.0 | Tests de capa de red |
| DataStore Preferences | 1.2.0 | Estado de onboarding y token de sesion |
| Coil | 2.7.0 | Carga de imagenes |
| Lottie Compose | 6.7.1 | Animaciones |
| Material Icons Extended | 1.7.8 | Iconografia |
| MockK | 1.13.16 | Mocks en unit tests |
| JaCoCo | 0.8.13 | Cobertura de tests |
| SonarQube | 7.2.2 | Analisis estatico y calidad |
| Gradle AGP | 9.1.0 | Build system |

Arquitectura: **Clean Architecture multi-modulo** con capas `core / domain / data / feature` aisladas a nivel de Gradle. La separacion no es solo de carpetas: el modulo `:domain` ni siquiera tiene Android en su classpath, lo que garantiza que la logica de dominio se mantiene pura por construccion del proyecto.

## Estructura del proyecto

```
traveling-app-frontend-android/
├── app/                         # Punto de entrada, MainActivity, NavGraph, modulos Hilt raiz
├── build-logic/
│   └── convention/              # Convention plugins propios (AndroidLibrary, AndroidFeature, AndroidHilt)
├── core/
│   ├── common/                  # Request/Response DTOs, FrameworkResponse, extensions, PasswordHasher
│   ├── network/                 # NetworkExecutor, INetworkChecker, AndroidNetworkChecker
│   ├── database/                # Room: AppDatabase, entities, DAOs (incluido PendingOperation)
│   ├── datastore/               # TokenManager, StoreBoarding (estado de onboarding)
│   └── ui/                      # Tema (colores, tipografia, Dimens) + wrappers de componentes
├── domain/                      # Entidades de dominio + interfaces de repositorio (sin Android)
├── data/                        # Implementaciones de repositorios, mappers, servicios remotos, sync
└── feature/
    ├── auth/                    # Login, Registro, AuthViewModel
    ├── onboarding/              # Pager de bienvenida
    └── home/                    # Tabs (Home, Hotel, Transport), Destinations CRUD, RentCar
```

### Reglas de dependencia

- `:domain` no depende de nada (ni Android, ni `:data`, ni `:feature`).
- `:data` depende de `:domain` y de los `:core:*` que necesite (network, database).
- Los `:feature:*` dependen de `:domain`, `:core:ui` y `:core:common`. Nunca acceden a `:data` directamente.
- `:app` depende de todos los modulos y compone el grafo Hilt en `core/di/AppModule.kt` y `core/di/DatabaseModule.kt`.

### Flujo de datos

```
UI (Compose) -> ViewModel -> IRepository (domain) -> Repository (data) -> IService (Retrofit) | DAO (Room)
                                                                                              \
                                                                                               -> SyncManager + WorkManager (outbox)
```

## Convention plugins

Los modulos library reutilizan configuracion comun via plugins en `build-logic/convention/`:

- `AndroidLibraryConventionPlugin` — aplica `com.android.library` + Kotlin con `compileSdk=36`, `minSdk=29`, JVM 11.
- `AndroidFeatureConventionPlugin` — extiende el de library añadiendo Compose, Hilt y dependencias comunes para modulos `:feature:*`.
- `AndroidHiltConventionPlugin` — aplica el plugin de Hilt + KSP del compilador.

Asi se evita repetir la misma configuracion en cada `build.gradle.kts`.

## Componentes UI propios

En `core/ui/src/main/java/.../ui/views/components/` viven los wrappers que las pantallas deben consumir en lugar de Material 3 directamente:

- Botones: `TravelPrimaryButton`, `TravelSecondaryButton`, `TravelTextButton`, `TravelLinkButton`, `AppIconButton`
- Texto e inputs: `AppText`, `AppTextField`
- Estructura: `AppToolBar`, `AppSpacer`, `TravelCard`
- Estado y feedback: `LoaderData`, `PagerIndicator`
- Multimedia: `AppImages`

Cada wrapper consume tokens centralizados de `core/ui/theme/Dimens.kt` (espaciado, radios, tamaños) y la paleta de `core/ui/theme/Color.kt`. La regla es: **si necesitas un componente nuevo, primero se crea el wrapper con su `@Preview` y despues se usa en la pantalla**.

> Estado actual: la cobertura de wrappers no es total. Algunos features todavia importan Material 3 directamente para `Scaffold`, `AlertDialog`, `Checkbox`, `CircularProgressIndicator`, `FloatingActionButton`, `NavigationBar`, etc. Se iran sustituyendo conforme se toquen esas pantallas.

## Modo offline y sincronizacion

El proyecto implementa un **Outbox Pattern** para garantizar que las operaciones del usuario no se pierdan sin conexion:

1. La capa `data/repository/*` escribe primero en la base de datos local (Room).
2. Si no hay red, la operacion se persiste como `PendingOperationEntity` en la cola de outbox.
3. `SyncManager` programa un `SyncWorker` (WorkManager) con restriccion de red.
4. Cuando vuelve la conectividad, el worker drena la outbox contra la API y reconcilia estados.

`INetworkChecker` (implementado por `AndroidNetworkChecker`) decide si una llamada va directa a la API o pasa por la cola.

## Autenticacion y roles

- Login y registro contra la API via `IAccountService`.
- Token persistido en DataStore (`TokenManager`).
- Roles soportados: **cliente** y **negocio**, lo que condiciona la navegacion y las acciones disponibles tras login.
- Hash de password con `PasswordHasher` (`core/common/security`) antes de enviar.

## Navegacion

`Routes.kt` define las rutas como `sealed interface Route` con `@Serializable` (kotlinx-serialization), de forma que Navigation Compose las consume tipadas:

```
Splash -> OnBoarding -> Login | Register -> Home -> { RentCar, DestinationDetail(id), CreateDestination }
```

Un unico `NavHost` vive en `app/src/main/.../navigation/NavManager.kt`.

## Calidad

```bash
./gradlew qualityCheck                # lint + unit tests + cobertura + verificacion de threshold
./gradlew :app:jacocoTestReport       # informe HTML/XML de cobertura
./gradlew :app:jacocoTestCoverageVerification
./gradlew sonar                       # analisis SonarQube (requiere SONAR_HOST_URL, SONAR_TOKEN)
```

El umbral minimo de cobertura de linea esta en `gradle.properties` bajo `quality.minLineCoverage` (por defecto 0.08, ajustable). La regla rompe el build si no se cumple.

Las exclusiones de cobertura (UI, Hilt generado, datastore, DAOs, etc.) estan documentadas en `app/build.gradle.kts` -> `jacocoExclusions`.

## Testing

| Capa | Donde | Herramientas |
|---|---|---|
| Unit | `core/*/src/test`, `data/src/test`, `feature/auth/src/test` | JUnit 4, MockK, kotlinx-coroutines-test |
| Red | `core/network/src/test` | OkHttp MockWebServer |
| Integracion | `app/src/androidTest`, `core/database/src/androidTest` | AndroidX Test, Room in-memory |
| UI | `app/src/androidTest` | Compose UI Test (`ui-test-junit4`) |

Estructura de tests siempre con patron **AAA** (Arrange / Act / Assert) en bloques visualmente separados.

## Requisitos

- Android Studio **Ladybug** o superior.
- JDK **11**.
- Android SDK **36** instalado.
- Dispositivo fisico o emulador con **API 29+** (Android 10).
- API accesible. Las URLs se separan por tipo de build:

`debug` lee las URLs locales desde `local.properties`, que no se versiona:

```properties
traveling.debug.backend.baseUrl=http://192.168.1.66:5090/
traveling.debug.mock.baseUrl=https://01394d44-8918-4a1d-8059-629c50c25e87.mock.pstmn.io/
```

`release` lee las URLs versionadas desde `gradle.properties`:

```properties
traveling.release.backend.baseUrl=http://192.168.1.66:5090/
traveling.release.mock.baseUrl=https://01394d44-8918-4a1d-8059-629c50c25e87.mock.pstmn.io/
```

Si no defines `traveling.debug.backend.baseUrl`, Gradle usa `http://10.0.2.2:5090/`, que apunta al host desde un emulador Android.

## Compilar y ejecutar

```bash
./gradlew assembleDebug          # APK debug (con suffix .dev)
./gradlew installDebug           # instala en el dispositivo conectado
./gradlew :app:assembleRelease   # APK release
```

Tambien se puede ejecutar directamente desde Android Studio con el run config `app`.

### Build types

| Tipo | applicationId | versionName |
|---|---|---|
| `debug` | `org.example.travelingapp.dev` | `1.0-dev` |
| `release` | `org.example.travelingapp` | `1.0` |

Esto permite tener instaladas a la vez la app de desarrollo y la de produccion en el mismo dispositivo.

## Internacionalizacion

| Idioma | Recurso |
|---|---|
| Español (por defecto) | `**/res/values/strings.xml` |
| Ingles | `**/res/values-es/strings.xml` |

Cada modulo `:feature:*` y `:core:ui` tiene sus propios `strings.xml` traducidos. El idioma se selecciona desde los ajustes del sistema operativo.

## Permisos declarados

- `INTERNET`, `ACCESS_NETWORK_STATE` — para Retrofit y el `NetworkChecker`.
- `CAMERA` — captura de imagenes (perfil, comprobantes).
- `ACCESS_COARSE_LOCATION`, `ACCESS_FINE_LOCATION` — ubicacion para destinos cercanos.
- `WRITE_EXTERNAL_STORAGE` — solo hasta API 28 (declarado con `maxSdkVersion`).

## Autor

Jose Luis Ortega.
