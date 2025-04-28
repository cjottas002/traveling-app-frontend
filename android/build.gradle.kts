// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.sonarqube)
}

sonar {
    properties {
        property("sonar.projectKey", System.getenv("SONAR_PROJECT_KEY") ?: "TravelingApp-Android")
        property("sonar.projectName", "TravelingApp-Android")
        property("sonar.host.url", System.getenv("SONAR_HOST_URL") ?: "http://localhost:9000")
        System.getenv("SONAR_TOKEN")
            ?.takeIf { it.isNotBlank() }
            ?.let { property("sonar.token", it) }
        property("sonar.sources", "app/src/main/java,app/src/main/kotlin")
        property("sonar.tests", "app/src/test/java,app/src/androidTest/java")
        property("sonar.junit.reportPaths", "app/build/test-results/testDebugUnitTest")
        property(
            "sonar.coverage.jacoco.xmlReportPaths",
            "app/build/reports/jacoco/jacocoTestReport/jacocoTestReport.xml"
        )
        property("sonar.androidLint.reportPaths", "app/build/reports/lint-results-debug.xml")
    }
}

tasks.register("qualityCheck") {
    group = "verification"
    description = "Runs lint, unit tests and coverage verification on Android app."
    dependsOn(
        ":app:lintDebug",
        ":app:testDebugUnitTest",
        ":app:jacocoTestReport",
        ":app:jacocoTestCoverageVerification"
    )
}
