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
import org.jmailen.gradle.kotlinter.tasks.FormatTask
import org.jmailen.gradle.kotlinter.tasks.LintTask

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.licenses)
    alias(libs.plugins.detekt)
    alias(libs.plugins.kotlinter)
}

kotlin {

    wasmJs {
        browser()
        binaries.executable()
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":ui"))
                implementation(project(":domain"))
                implementation(project(":presentation"))
                implementation(libs.compose.ui)
                implementation(libs.compose.resources)
                implementation(libs.compose.material)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.koin.core)
                implementation(libs.koin.compose)
                implementation(libs.koin.viewmodel)
                implementation(libs.napier)
            }
        }
    }
}

tasks.withType<LintTask> {
    exclude { it.file.path.contains("generated") }
}

tasks.withType<FormatTask> {
    exclude { it.file.path.contains("generated") }
}

dependencies {
    detektPlugins(libs.detekt.rules.compose)
}
