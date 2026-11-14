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
    kotlin("plugin.serialization")
}

kotlin {
    android {
        namespace = "fobo66.valiutchik.data.impl"

        packaging {
            jniLibs.pickFirsts.add("lib/**/libc++_shared.so")
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                api(project(":data:api"))
                implementation(project(":api:api"))
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.androidx.annotation)
                implementation(libs.androidx.collection)
                implementation(libs.aboutlibraries.core)
                implementation(libs.koin.core)
                implementation(libs.kotlinx.serialization)
                implementation(libs.kotlinx.serialization.io)
                implementation(libs.kotlinx.io)
                implementation(libs.kotlinx.datetime)
                implementation(libs.sqlidelight.androidx)
                implementation(libs.androidx.sqlite)
                implementation(libs.sqlidelight.coroutines)
                implementation(libs.androidx.datastore.core)
                implementation(libs.napier)
                implementation(libs.uri)
            }
        }

        commonTest {
            dependencies {
                implementation(kotlin("test"))
                implementation(project(":data:testing"))
                implementation(libs.kotlinx.coroutines.test)
                implementation(libs.turbine)
            }
        }

        named("desktopMain") {
            dependencies {
                implementation(libs.icu)
                implementation(libs.sqlidelight.jvm)
            }
        }

        named("desktopTest") {
            dependencies {
                implementation(libs.truth)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.koin.android)
                implementation(libs.androidx.datastore)
                implementation(libs.sqlidelight.android)
            }
        }

        webMain {
            dependencies {
                implementation(libs.kotlinx.browser)
                implementation(libs.doistx.normalize)
                implementation(libs.androidx.sqlite.web)
                implementation(libs.sqlidelight.js)
                implementation(libs.sqlidelight.androidx.web)
            }
        }

        named("androidHostTest") {
            dependencies {
                implementation(libs.truth)
                implementation(libs.koin.test)
                implementation(libs.ktor.client)
            }
        }

        named("androidDeviceTest") {
            dependencies {
                implementation(libs.truth)
                implementation(libs.androidx.test.rules)
                implementation(libs.androidx.test.junit)
                implementation(libs.androidx.test.truth)
                implementation(libs.androidx.test.espresso.intents)
            }
        }
    }
}
