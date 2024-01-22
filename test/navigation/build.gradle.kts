@Suppress("DSL_SCOPE_VIOLATION") // Remove when fixed https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    alias(libs.plugins.countdowns.android.test)
}

android {
    namespace = "com.craft.apps.countdowns.test.navigation"
    targetProjectPath = ":app:mobile"
}

dependencies {
    implementation(project(":app:mobile"))
    implementation(project(":core:testing"))
    implementation(project(":data"))
    implementation(project(":feature:list"))
    implementation(project(":feature:details"))
}
