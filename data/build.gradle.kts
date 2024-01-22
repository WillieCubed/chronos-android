@Suppress("DSL_SCOPE_VIOLATION") // Remove when fixed https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    alias(libs.plugins.countdowns.android.library)
}

android {
    namespace = "com.craft.apps.countdowns.core.data"
}

dependencies {
    api(project(":core:model"))
    api(project(":core:database"))
    implementation(project(":core:coroutines"))
    implementation(libs.kotlinx.coroutines.android)

    // Unit testing
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
}
