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
    alias(libs.plugins.sqlidelight)
}

kotlin {
    android {
        namespace = "fobo66.valiutchik.core"

        packaging {
            jniLibs.pickFirsts.add("lib/**/libc++_shared.so")
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":api:api"))
                implementation(libs.kotlinx.io)
                implementation(libs.aboutlibraries.core)
                implementation(libs.uri)
            }
        }

        commonTest {
            dependencies {
                implementation(kotlin("test"))
                implementation(project(":data:testing"))
            }
        }
    }
}

sqldelight {
    databases {
        create("Database") {
            packageName = "dev.fobo66.valiutchik.core.db"
            generateAsync = true
        }
    }
}
