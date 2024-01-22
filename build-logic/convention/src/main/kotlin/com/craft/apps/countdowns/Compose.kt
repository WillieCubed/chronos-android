package com.craft.apps.countdowns

import com.android.build.gradle.BaseExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.project

@Suppress("UnstableApiUsage")
internal fun Project.configureCompose(commonExtension: BaseExtension) {
    commonExtension.apply {
        buildFeatures.apply {
            compose = true
        }

        val libs: VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

        composeOptions {
            kotlinCompilerExtensionVersion =
                libs.findVersion("androidxComposeCompiler").get().toString()
        }

        dependencies {
            add("implementation", platform(libs.findLibrary("androidx.compose.bom").get()))
            // TODO: Add this back in once I can figure out what I'm doing wrong
            // WHAT DO YOU MEAN "Could not find androidx.compose.ui:ui-test-junit4:."???
//            add("androidTestImplementation", libs.findLibrary("androidx.compose.ui.test.junit4").get())
            add("androidTestImplementation", project(":core:testing"))
        }
    }
}
