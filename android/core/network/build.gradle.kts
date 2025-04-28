plugins {
    id("travelingapp.android.library")
    id("travelingapp.android.hilt")
}

android {
    namespace = "org.example.travelingapp.core.network"
}

dependencies {
    implementation(project(":core:common"))
    implementation(libs.androidx.core.ktx)

    testImplementation(libs.junit)
}
