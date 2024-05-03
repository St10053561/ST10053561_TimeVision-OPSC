pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        //Adding the jetpack repository for charts
        maven("https://jitpack.io")

        //Adding the repository for JitPack
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "TimeVision_Application"
include(":app")
