/*
 *    Copyright 2026 Andrey Mukamolov
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

@file:OptIn(ExperimentalWasmDsl::class)

import com.android.sdklib.AndroidVersion
import dev.detekt.gradle.Detekt
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library.multiplatform)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.detekt)
    alias(libs.plugins.kotlinter)
}

kotlin {
    jvm("desktop") {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_17
        }
    }

    android {
        namespace = "fobo66.valiutchik.api"
        compileSdk {
            version = release(37)
        }

        minSdk {
            version = release(AndroidVersion.VersionCodes.R)
        }

        compilations.configureEach {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget = JvmTarget.JVM_17
                }
            }
        }
    }

    wasmJs {
        browser()
    }

    sourceSets {
        commonMain {
            generateSecrets(project)
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.androidx.collection)

                implementation(libs.koin.core)

                implementation(libs.ktor.client)
                implementation(libs.ktor.auth)
                implementation(libs.ktor.content)
                implementation(libs.ktor.encoding)
                implementation(libs.ktor.logging)
                implementation(libs.ktor.serialization)
                implementation(libs.kotlinx.serialization)
                implementation(libs.kotlinx.serialization.io)
                implementation(libs.kotlinx.io)

                implementation(libs.napier)
            }
            kotlin.srcDir(project.layout.buildDirectory.dir("generated/source/secret"))
        }

        webMain {
            dependencies {
                implementation(libs.ktor.client.js)
            }
        }

        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        named("desktopTest") {
            dependencies {
                implementation(libs.koin.test)
            }
        }
    }
}

detekt {
    autoCorrect = true
}

tasks.withType<Detekt> {
    jvmTarget = "17"
}

dependencies {
    detektPlugins(libs.detekt.rules.compose)
}
