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

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id("buildlogic.library-conventions")
    id("buildlogic.secrets-conventions")
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    android {
        namespace = "fobo66.valiutchik.api"
    }

    sourceSets {
        commonMain {
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
