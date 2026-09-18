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

@file:OptIn(ExperimentalWasmDsl::class, ExperimentalMetroGradleApi::class)

import dev.zacsweers.metro.gradle.ExperimentalMetroGradleApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id("buildlogic.library-conventions")
    alias(libs.plugins.android.lint)
    alias(libs.plugins.metro)
    alias(libs.plugins.compose)
}

kotlin {
    android {
        namespace = "dev.fobo66.valiutchik.presentation.impl"
    }

    wasmJs {
        browser {
            testTask {
                enabled = false
            }
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":domain:api"))
                api(project(":presentation:api"))
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.circuit.foundation)
                implementation(libs.kotlinx.collections)
                implementation(libs.kotlinx.datetime)
                implementation(libs.androidx.lifecycle.viewmodel)
                implementation(libs.metrox.viewmodel)
                implementation(libs.napier)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(project(":data:testing"))
                implementation(project(":data:api"))
                implementation(project(":domain:testing"))
                implementation(libs.turbine)
                implementation(libs.kotlinx.coroutines.test)
            }
        }
    }
}

metro {
    enableCircuitCodegen = true
}
