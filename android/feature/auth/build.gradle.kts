plugins {
    id("travelingapp.android.feature")
}

android {
    namespace = "org.example.travelingapp.feature.auth"
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":core:common"))
    implementation(project(":core:ui"))
    implementation(project(":core:network"))
    implementation(project(":core:datastore"))

    implementation(libs.coil.compose)
}
