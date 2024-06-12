pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
    }

    plugins {
        kotlin("multiplatform") version "2.0.0"
        id("com.android.library") version "8.2.2"
        id("org.jetbrains.dokka") version "1.9.20"
    }
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
        google()
    }
}

rootProject.name = "Log4K"

include(":log4k")
include(":log4k-slf4j")
