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
    alias(libs.plugins.android.lint)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.multiplatform)
}

kotlin {
    android {
        namespace = "dev.fobo66.valiutchik.ui"

        experimentalProperties["android.experimental.kmp.enableAndroidResources"] = true
    }

    wasmJs {
        binaries.executable()
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":presentation:api"))
                implementation(project(":domain:api"))
                implementation(libs.androidx.lifecycle.compose)
                implementation(libs.materialKolor)
                implementation(libs.compose.runtime)
                implementation(libs.compose.foundation)
                implementation(libs.compose.ui)
                implementation(libs.compose.material)
                implementation(libs.compose.resources)
                implementation(libs.compose.ui.preview)
                implementation(libs.kotlinx.collections)
                implementation(libs.compose.material.adaptive)
                implementation(libs.compose.material.adaptive.layout)
                implementation(libs.compose.material.adaptive.navigation)
                implementation(libs.koin.core)
                implementation(libs.koin.viewmodel)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.compose.ui.test)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.compose.ui.preview)
                implementation(libs.compose.ui.tooling)
                implementation(libs.accompanist.permissions)
            }
        }

        named("desktopMain") {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(libs.compose.ui.tooling)
            }
        }

        named("desktopTest") {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }

        named("androidDeviceTest") {
            dependencies {
                implementation(libs.androidx.test.runner)
                implementation(libs.androidx.test.junit)
                implementation(libs.compose.ui.testing.manifest)
            }
        }
    }
}

detekt {
    config.setFrom(rootProject.file("config/detekt/compose.yml"))
}

dependencies {
    detektPlugins(libs.detekt.rules.compose)
}
