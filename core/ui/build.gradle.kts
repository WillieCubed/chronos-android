@Suppress("DSL_SCOPE_VIOLATION") // Remove when fixed https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    alias(libs.plugins.countdowns.android.library)
    alias(libs.plugins.countdowns.compose)
}

android {
    namespace = "com.craft.apps.countdowns.core.ui"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:util"))

    implementation(libs.kotlinx.datetime)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.material.icons)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material3.windowsizeclass)
    implementation(libs.androidx.ui.tooling.preview.android)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    debugImplementation(libs.androidx.ui.tooling)
    implementation(libs.material.components)
}
