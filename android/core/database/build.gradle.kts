plugins {
    id("travelingapp.android.library")
    alias(libs.plugins.ksp)
}

android {
    namespace = "org.example.travelingapp.core.database"
}

dependencies {
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
