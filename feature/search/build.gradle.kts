@Suppress("DSL_SCOPE_VIOLATION") // Remove when fixed https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    alias(libs.plugins.countdowns.android.library)
    alias(libs.plugins.countdowns.compose)
}

android {
    namespace = "com.craft.apps.countdowns.feature.search"
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:coroutines"))
    implementation(project(":data"))

    implementation(libs.androidx.appsearch)
    implementation(libs.androidx.appsearch.localstorage)
    implementation(libs.androidx.appsearch.platformstorage)
    kapt(libs.androidx.appsearch.compiler)

    // Compose
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.hilt.navigation.compose)
}
