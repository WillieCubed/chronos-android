@Suppress("DSL_SCOPE_VIOLATION") // Remove when fixed https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    alias(libs.plugins.countdowns.android.application)
    alias(libs.plugins.countdowns.compose)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.craft.apps.countdowns"

    defaultConfig {
        applicationId = "com.craft.apps.countdowns"
    }

    dynamicFeatures += setOf(":dynamic")
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":feature:home"))
    implementation(project(":feature:details"))
    implementation(project(":feature:search"))
    implementation(project(":feature:widgets"))

    // Core Android dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Arch Components
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)

    // Compose
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)

    implementation(libs.google.play.feature.delivery)
    implementation(libs.androidx.ui.tooling.preview.android)
    implementation(libs.androidx.hilt.work)
}
