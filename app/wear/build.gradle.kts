@Suppress("DSL_SCOPE_VIOLATION")  // Remove when fixed https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    alias(libs.plugins.countdowns.android.application)
    alias(libs.plugins.countdowns.compose)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.craft.apps.countdowns.wear"

    defaultConfig {
        applicationId = "com.craft.apps.countdowns.wear"
    }
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:ui-wear"))
    implementation(project(":feature:wear:home"))
    implementation(project(":feature:wear:tiles"))
    implementation(project(":feature:wear:complications"))

    implementation(libs.androidx.core.splashscreen)
    // Compose
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.wear.compose.material)
    implementation(libs.androidx.wear.compose.navigation)
    implementation(libs.androidx.ui.tooling.preview.android)

}
