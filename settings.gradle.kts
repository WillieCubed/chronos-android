@file:Suppress("UnstableApiUsage")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Chronos"

include(":app:mobile")
include(":app:wear")
include(":core:coroutines")
include(":core:lib")
include(":core:database")
include(":core:testing")
include(":core:ui")
include(":core:ui-wear")
include(":core:analytics")
include(":core:model")
include(":core:util")
include(":data")
include(":dynamic")
include(":feature:details")
include(":feature:home")
include(":feature:search")
include(":feature:widgets")
include(":feature:wear:complications")
include(":feature:wear:home")
include(":feature:wear:tiles")
// TODO: Re-enable once we figure out why build-logic testing plugin is broken
//include(":test:navigation")
