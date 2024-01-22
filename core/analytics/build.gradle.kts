plugins {
    alias(libs.plugins.countdowns.android.library)
}

android {
    namespace = "com.craft.apps.countdowns.core.analytics"
}

dependencies {
    implementation(project(":core:model"))

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
}