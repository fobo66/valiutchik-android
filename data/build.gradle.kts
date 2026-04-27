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
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library.multiplatform)
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    alias(libs.plugins.detekt)
    alias(libs.plugins.sqlidelight)
}

kotlin {
    jvm {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_17
        }
    }

    android {
        namespace = "fobo66.valiutchik.core"
        compileSdk {
            version = release(37)
        }

        minSdk {
            version = release(AndroidVersion.VersionCodes.R)
        }

        withHostTest {}
        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }

        compilations.configureEach {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget = JvmTarget.JVM_17
                }
            }
        }

        packaging {
            jniLibs.pickFirsts.add("lib/**/libc++_shared.so")
        }
    }

    wasmJs {
        browser {
            testTask {
                useKarma {
                    useFirefoxHeadless()
                }
            }
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":api"))
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.androidx.annotation)
                implementation(libs.androidx.collection)
                implementation(libs.aboutlibraries.core)
                implementation(libs.koin.core)
                implementation(libs.kotlinx.serialization)
                implementation(libs.kotlinx.serialization.io)
                implementation(libs.kotlinx.io)

                implementation(libs.kotlinx.datetime)
                implementation(libs.sqlidelight.coroutines)
                implementation(libs.androidx.datastore.core)
                implementation(libs.napier)
                implementation(libs.uri)
            }
        }

        commonTest {
            dependencies {
                implementation(kotlin("test"))
                implementation(project(":data-testing"))
                implementation(libs.kotlinx.coroutines.test)
            }
        }

        jvmMain {
            dependencies {
                implementation(libs.icu)
                implementation(libs.sqlidelight.jvm)
            }
        }

        jvmTest {
            dependencies {
                implementation(libs.koin.test)
                implementation(libs.truth)
                implementation(libs.turbine)
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
                implementation(libs.sqlidelight.js)
                implementation(libs.sqlidelight.web)
                implementation(npm("sql.js", "1.14.1"))
                implementation(devNpm("copy-webpack-plugin", "14.0.0"))
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

sqldelight {
    databases {
        create("Database") {
            packageName = "dev.fobo66.valiutchik.core.db"
            generateAsync = true
        }
    }
}

detekt {
    autoCorrect = true
}

dependencies {
    detektPlugins(libs.detekt.rules.compose)
}
