plugins {
    id("travelingapp.android.library")
    id("travelingapp.android.hilt")
}

android {
    namespace = "org.example.travelingapp.core.datastore"
}

dependencies {
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.core.ktx)
}
