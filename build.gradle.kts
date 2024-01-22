buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.android.tools.build.gradleplugin)
        classpath(libs.kotlin.gradleplugin)
    }
}

plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.countdowns.android.application) apply false
    alias(libs.plugins.countdowns.android.library) apply false
    alias(libs.plugins.countdowns.android.test) apply false
    alias(libs.plugins.countdowns.compose) apply false
    alias(libs.plugins.countdowns.dynamic) apply false
    alias(libs.plugins.playservices.gradleplugin) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt.gradleplugin) apply false
}
