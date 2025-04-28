plugins {
    id("travelingapp.android.library")
}

android {
    namespace = "org.example.travelingapp.core.common"
}

dependencies {
    implementation(libs.converter.gson)

    testImplementation(libs.junit)
}
