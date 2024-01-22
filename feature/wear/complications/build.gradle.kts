@Suppress("DSL_SCOPE_VIOLATION") // Remove when fixed https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    alias(libs.plugins.countdowns.android.library)
    alias(libs.plugins.countdowns.compose)
}

android {
    namespace = "com.craft.apps.countdowns.feature.wear.complications"
}

dependencies {
    implementation(project(":data"))
    implementation(project(":core:ui"))

    // Arch Components
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.hilt.navigation.compose)

    // Compose
    implementation(libs.androidx.wear.compose.material)
    implementation(libs.androidx.watchface.complications.data.source.ktx)

    implementation(libs.horologist.compose.tools)
    implementation(libs.horologist.tiles)
}
