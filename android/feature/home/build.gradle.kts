plugins {
    id("travelingapp.android.feature")
}

android {
    namespace = "org.example.travelingapp.feature.home"
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":core:common"))
    implementation(project(":core:ui"))

    implementation(libs.coil.compose)
    implementation(libs.material.icons.extended)
}
