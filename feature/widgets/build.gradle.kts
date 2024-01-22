@Suppress("DSL_SCOPE_VIOLATION") // Remove when fixed https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    alias(libs.plugins.countdowns.android.library)
    alias(libs.plugins.countdowns.compose)
}

android {
    namespace = "com.craft.apps.countdowns.feature.widgets"
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:coroutines"))
    implementation(project(":core:util"))
    implementation(project(":core:analytics"))
    implementation(project(":data"))
    implementation(libs.androidx.glance.appwidget)
    implementation(libs.androidx.glance.material3)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.hilt.common)

    // Compose
    implementation(libs.androidx.compose.ui)
//    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.material3)
//    implementation(libs.androidx.hilt.navigation.compose)
}
