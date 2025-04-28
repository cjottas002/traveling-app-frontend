plugins {
    id("travelingapp.android.library")
}

android {
    namespace = "org.example.travelingapp.domain"
}

dependencies {
    implementation(project(":core:common"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.converter.gson)
}
