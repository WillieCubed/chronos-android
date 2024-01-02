pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "LogDate"
//project(":craft-essentials").projectDir = new File(settingsDir, "craft-essentials/library")
include(":app", ":base") /*":instantapp",*/ /*":things",*/ /*":craft-essentials"*/
