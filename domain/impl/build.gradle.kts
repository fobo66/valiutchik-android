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

plugins {
    id("buildlogic.library-conventions")
    alias(libs.plugins.metro)
}

kotlin {
    android {
        namespace = "fobo66.valiutchik.domain.impl"
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":data:api"))
                api(project(":domain:api"))
                implementation(libs.androidx.annotation)
                implementation(libs.androidx.collection)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.datetime)
                implementation(libs.napier)
                implementation(libs.compose.stable.marker)
            }
        }

        commonTest {
            dependencies {
                implementation(kotlin("test"))
                implementation(project(":data:testing"))
                implementation(project(":domain:testing"))
                implementation(libs.turbine)
                implementation(libs.kotlinx.coroutines.test)
            }
        }
    }
}
