plugins {
    kotlin("multiplatform")
    id("com.android.library")
    `maven-publish`
    signing
}

kotlin {
    androidTarget { publishAllLibraryVariants() }
    iosArm64()
    iosSimulatorArm64()
    js {
        nodejs()
        compilations.all {
            kotlinOptions.sourceMap = true
            kotlinOptions.moduleKind = "umd"
        }
    }
    jvm { testRuns["test"].executionTask.configure { useJUnitPlatform() } }

    applyDefaultHierarchyTemplate()

    sourceSets {
        commonMain.dependencies { implementation(project(":log4k")) }
        commonTest.dependencies { implementation(kotlin("test")) }
        androidMain.dependencies { implementation("org.slf4j:slf4j-api:1.7.36") }
        jvmMain.dependencies { implementation("org.slf4j:slf4j-api:1.7.36") }
    }
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

android {
    namespace = "saschpe.log4k.slf4j"

    defaultConfig {
        compileSdk = 33
        minSdk = 17
    }

    testCoverage.jacocoVersion = "0.8.10"
}

group = "de.peilicke.sascha"
version = "1.2.3"

publishing {
    publications.withType<MavenPublication> {
        pom {
            name.set("Log4K-SLF4J")
            description.set("Lightweight logging library for Kotlin/Multiplatform - SLF4J integration. Supports Android, iOS, JavaScript and plain JVM environments.")
            url.set("https://github.com/saschpe/log4k")

            licenses {
                license {
                    name.set("MIT")
                    url.set("https://opensource.org/licenses/MIT")
                }
            }
            developers {
                developer {
                    id.set("saschpe")
                    name.set("Sascha Peilicke")
                    email.set("sascha@peilicke.de")
                }
            }
            scm {
                connection.set("scm:git:git://github.com/saschpe/log4k.git")
                developerConnection.set("scm:git:ssh://github.com/saschpe/log4k.git")
                url.set("https://github.com/saschpe/log4k")
            }
        }
    }

    repositories {
        maven {
            name = "sonatype"
            credentials {
                username = Secrets.Sonatype.user
                password = Secrets.Sonatype.apiKey
            }
            url = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2")
        }
    }
}

signing {
    val sonatypeGpgKey = System.getenv("SONATYPE_GPG_KEY")
    val sonatypeGpgKeyPassword = System.getenv("SONATYPE_GPG_KEY_PASSWORD")
    when {
        sonatypeGpgKey == null || sonatypeGpgKeyPassword == null -> useGpgCmd()
        else -> useInMemoryPgpKeys(sonatypeGpgKey, sonatypeGpgKeyPassword)
    }
    sign(publishing.publications)
}
