plugins {
    id("travelingapp.android.library")
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "org.example.travelingapp.core.ui"
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.material.icons.extended)
    implementation(libs.lottie.compose)
    implementation(libs.coil.compose)
    implementation(libs.androidx.navigation.compose)
}
