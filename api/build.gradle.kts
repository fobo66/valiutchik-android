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

import com.android.sdklib.AndroidVersion
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library.multiplatform)
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    alias(libs.plugins.detekt)
    alias(libs.plugins.kotlinter)
}

kotlin {
    jvm {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_17
        }
    }

    androidLibrary {
        namespace = "fobo66.valiutchik.api"
        compileSdk = AndroidVersion.VersionCodes.BAKLAVA

        minSdk = AndroidVersion.VersionCodes.R

        withHostTestBuilder {}.configure {}

        compilations.configureEach {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget = JvmTarget.JVM_17
                }
            }
        }
    }

    sourceSets {
        commonMain {
            generateSecrets(project)
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.androidx.collection)

                implementation(project.dependencies.platform(libs.koin.bom))
                implementation(libs.koin.core)

                implementation(project.dependencies.platform(libs.ktor.bom))
                implementation(libs.ktor.client)
                implementation(libs.ktor.auth)
                implementation(libs.ktor.content)
                implementation(libs.ktor.encoding)
                implementation(libs.ktor.logging)
                implementation(libs.ktor.serialization)
                implementation(libs.kotlinx.serialization)
                implementation(libs.kotlinx.io)

                implementation(libs.napier)
            }
            kotlin.srcDir(project.layout.buildDirectory.dir("generated/source/secret"))
        }

        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        jvmTest {
            dependencies {
                implementation(project.dependencies.platform(libs.koin.bom))
                implementation(libs.koin.test)
            }
        }

        androidMain {
            dependencies {
                implementation(project.dependencies.platform(libs.koin.bom))
                implementation(libs.koin.android)
                implementation(project.dependencies.platform(libs.ktor.bom))
                implementation(libs.ktor.logging)
            }
        }

        named("androidHostTest") {
            dependencies {
                implementation(project.dependencies.platform(libs.koin.bom))
                implementation(libs.koin.test)
            }
        }
    }
}

detekt {
    autoCorrect = true
}

dependencies {
    detektPlugins(libs.detekt.rules.compose)
}
