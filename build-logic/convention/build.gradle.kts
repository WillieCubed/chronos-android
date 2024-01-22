plugins {
    `kotlin-dsl`
}

group = "com.craft.apps.countdowns.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    compileOnly(libs.android.tools.build.gradleplugin)
    compileOnly(libs.kotlin.gradleplugin)
    compileOnly(libs.ksp.gradleplugin)
}

gradlePlugin {
    /**
     * Register convention plugins so they are available in the build scripts of the application
     */
    plugins {
        register("countdownsAndroidApplication") {
            id = "countdowns.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("countdownsAndroidLibrary") {
            id = "countdowns.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("countdownsAndroidTest") {
            id = "countdowns.android.test"
            implementationClass = "AndroidTestConventionPlugin"
        }
        register("countdownsCompose") {
            id = "countdowns.compose"
            implementationClass = "ComposeConventionPlugin"
        }
        register("countdownsDynamic") {
            id = "countdowns.dynamic"
            implementationClass = "DynamicFeatureConventionPlugin"
        }
    }
}
