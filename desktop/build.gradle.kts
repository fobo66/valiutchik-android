/*
 *    Copyright 2025 Andrey Mukamolov
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.hotreload)
    alias(libs.plugins.detekt)
    alias(libs.plugins.kotlinter)
}

kotlin {
    jvm("desktop") {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_17
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                api(project(":ui"))
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(libs.compose.material)
                implementation(libs.compose.material.icons.core)
                implementation(libs.kotlinx.coroutines.test)
                implementation(libs.kotlinx.coroutines.core)
                implementation(project.dependencies.platform(libs.koin.bom))
                implementation(libs.koin.core)
                implementation(libs.koin.compose)
                implementation(libs.koin.viewmodel)
                implementation(libs.napier)
            }
        }

        named("desktopMain") {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(libs.kotlinx.coroutines.swing)
                implementation(libs.slf4j)
                implementation(libs.logback)
                implementation(libs.jansi)
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "dev.fobo66.valiutchik.desktop.MainKt"

        jvmArgs("-Dapple.awt.application.appearance=system")

        buildTypes {
            release {
                proguard {
                    isEnabled = true
                    optimize = false 
                    obfuscate = true
                    configurationFiles.from(
                        project.layout.projectDirectory.file("proguard-rules.pro")
                    )
                }
            }
        }

        nativeDistributions {
            targetFormats(TargetFormat.Exe, TargetFormat.Deb, TargetFormat.Dmg)
            packageName = "dev.fobo66.valiutchik.desktop"
            packageVersion = "1.0.0"

            linux {
                iconFile = project.layout.projectDirectory.file("icons/ic_launcher.png")
            }
            macOS {
                iconFile = project.layout.projectDirectory.file("icons/ic_launcher.icns")
            }
            windows {
                iconFile = project.layout.projectDirectory.file("icons/ic_launcher.ico")
            }
        }
    }
}

dependencies {
    detektPlugins(libs.detekt.rules.compose)
}
