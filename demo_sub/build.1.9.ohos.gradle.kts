plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("com.google.devtools.ksp") version "1.9.23-1.0.20"
    id("maven-publish")
}

group = MavenConfig.GROUP
version = Version.getCoreVersion()

publishing {
    repositories {
        val username = MavenConfig.getUsername(project)
        val password = MavenConfig.getPassword(project)
        if (username.isNotEmpty() && password.isNotEmpty()) {
            maven {
                credentials {
                    setUsername(username)
                    setPassword(password)
                }
                url = uri(MavenConfig.getRepoUrl(version as String))
            }
        } else {
            mavenLocal()
        }
    }
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    ohosArm64 {
        binaries.sharedLib("shared") {
            if (debuggable) {
                freeCompilerArgs += "-Xadd-light-debug=enable"
                freeCompilerArgs += "-Xbinary=sourceInfoType=libbacktrace"
            } else {
                freeCompilerArgs += "-Xbinary=sourceInfoType=noop"
            }
        }
        val main by compilations.getting
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":core"))
                implementation(project(":compose"))
                implementation(project(":core-annotations"))
            }
        }
    }
}

dependencies {
    add("kspOhosArm64", project(":core-ksp"))
}

ksp {
    // 开启异常捕获
    arg("caughtException", "true")
}
