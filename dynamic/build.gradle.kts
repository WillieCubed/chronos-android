@Suppress("DSL_SCOPE_VIOLATION") // Remove when fixed https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    alias(libs.plugins.countdowns.dynamic)
}

android {
    namespace = "com.craft.apps.countdowns.dynamic"
}

dependencies {
    implementation(project(":app:mobile"))
}
