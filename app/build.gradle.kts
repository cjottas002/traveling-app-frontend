import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.gradle.testing.jacoco.tasks.JacocoCoverageVerification
import org.gradle.testing.jacoco.tasks.JacocoReport
import java.math.BigDecimal

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
    jacoco
}

android {
    namespace = "org.example.travelingapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "org.example.travelingapp"
        minSdk = 29
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
    }
}

dependencies {
    // Modules
    implementation(project(":core:common"))
    implementation(project(":core:network"))
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))
    implementation(project(":core:ui"))
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":feature:auth"))
    implementation(project(":feature:home"))
    implementation(project(":feature:onboarding"))

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.hilt.work)
    ksp(libs.hilt.compiler)
    ksp(libs.androidx.hilt.work.compiler)

    // WorkManager
    implementation(libs.androidx.work.runtime)

    // Room (needed for DatabaseModule in app)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)

    // Adaptive / WindowSizeClass
    implementation(libs.androidx.material3.window.size)

    // DataStore
    implementation(libs.androidx.datastore.preferences)

    // Retrofit (needed for AppModule in app)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    testImplementation(libs.okhttp.mockwebserver)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

jacoco {
    toolVersion = libs.versions.jacoco.get()
}

val jacocoExclusions = listOf(
    "**/R.class",
    "**/R$*.class",
    "**/BuildConfig.*",
    "**/Manifest*.*",
    "**/*Test*.*",
    "**/*\$Companion*.*",
    "**/*\$Lambda$*.*",
    "**/*\$inlined$*.*",
    "**/*\$serializer*.*",
    "**/di/**",
    "**/hilt_aggregated_deps/**",
    "**/*Hilt*.*",
    "**/*_Factory*.*",
    "**/*_MembersInjector*.*",
    // UI/presentation and Android framework adapters are validated by UI/instrumented tests.
    "**/ui/**",
    "**/navigation/**",
    "**/TravelingApp*.*",
    "**/core/datastore/**",
    "**/data/local/AppDatabase*.*",
    "**/data/local/daos/**",
    "**/core/network/AndroidNetworkChecker*.*"
)

val mainSources = listOf(
    "$projectDir/src/main/java",
    "$projectDir/src/main/kotlin"
)
val buildDirFile = layout.buildDirectory.get().asFile
val jacocoClassDirectories = files(
    fileTree("$buildDirFile/intermediates/classes/debug/transformDebugClassesWithAsm/dirs") {
        exclude(jacocoExclusions)
    },
    fileTree("$buildDirFile/tmp/kotlin-classes/debug") {
        exclude(jacocoExclusions)
    },
    fileTree("$buildDirFile/intermediates/javac/debug/compileDebugJavaWithJavac/classes") {
        exclude(jacocoExclusions)
    },
    fileTree("$buildDirFile/intermediates/javac/debug/classes") {
        exclude(jacocoExclusions)
    }
)
val minimumLineCoverage = providers
    .gradleProperty("quality.minLineCoverage")
    .map(String::toBigDecimal)
    .orElse(BigDecimal("0.08"))

tasks.register<JacocoReport>("jacocoTestReport") {
    group = "verification"
    description = "Generates JaCoCo coverage report for Debug unit tests."
    dependsOn("testDebugUnitTest")

    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(false)
    }

    classDirectories.setFrom(jacocoClassDirectories)
    sourceDirectories.setFrom(files(mainSources))
    additionalSourceDirs.setFrom(files(mainSources))
    executionData.setFrom(
        fileTree(buildDirFile) {
            include(
                "jacoco/testDebugUnitTest.exec",
                "outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec",
                "outputs/code_coverage/debugAndroidTest/connected/**/*.ec"
            )
        }
    )
}

tasks.register<JacocoCoverageVerification>("jacocoTestCoverageVerification") {
    group = "verification"
    description = "Fails build if Debug unit test coverage is below configured threshold."
    dependsOn("jacocoTestReport")

    classDirectories.setFrom(jacocoClassDirectories)
    sourceDirectories.setFrom(files(mainSources))
    executionData.setFrom(
        fileTree(buildDirFile) {
            include(
                "jacoco/testDebugUnitTest.exec",
                "outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec",
                "outputs/code_coverage/debugAndroidTest/connected/**/*.ec"
            )
        }
    )

    violationRules {
        rule {
            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = minimumLineCoverage.get()
            }
        }
    }
}

tasks.named("check") {
    dependsOn("jacocoTestReport", "jacocoTestCoverageVerification")
}
