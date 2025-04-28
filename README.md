# traveling-app-frontend

Aplicaciones cliente para la gestion de viajes.

## Plataformas

| Plataforma | Directorio | Estado |
|------------|-----------|--------|
| Android | `android/` | Activo |
| iOS | `ios/` | Pendiente |
| Web (Angular) | `web/` | Pendiente |

## Android

Aplicacion nativa desarrollada con Kotlin y Jetpack Compose.

### Tecnologias

- Kotlin + Jetpack Compose
- MVVM
- Retrofit + Dagger Hilt
- Room (base de datos local)
- Modularizacion por capas

### Estructura

```
android/
  app/         Punto de entrada, DI (Hilt)
  core/        Modelos compartidos, base de datos local
  data/        Servicios remotos, mappers, repositorios
  domain/      Entidades de dominio, casos de uso
  feature/     Pantallas por funcionalidad
```

### Ejecucion

```bash
cd android
./gradlew assembleDebug
```

### Calidad

```bash
cd android
./gradlew qualityCheck
./gradlew :app:jacocoTestReport
./gradlew sonar
```
