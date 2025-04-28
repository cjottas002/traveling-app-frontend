plugins {
    id("travelingapp.android.feature")
}

android {
    namespace = "org.example.travelingapp.feature.onboarding"
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":core:common"))
    implementation(project(":core:ui"))
    implementation(project(":core:datastore"))
}
