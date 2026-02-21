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

import com.android.sdklib.AndroidVersion
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library.multiplatform)
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    alias(libs.plugins.ksp)
    alias(libs.plugins.detekt)
    alias(libs.plugins.room)
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
            version = release(AndroidVersion.VersionCodes.BAKLAVA) {
                minorApiLevel = 1
            }
        }

        minSdk {
            version = release(AndroidVersion.VersionCodes.R)
        }

        withHostTest {}
        withDeviceTestBuilder {
            sourceSetTreeName = "test"
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
                implementation(libs.room.runtime)
                implementation(libs.androidx.datastore)
                implementation(libs.napier)
                implementation(libs.uri)
            }
        }

        commonTest {
            dependencies {
                implementation(kotlin("test"))
                implementation(project(":data-testing"))
                implementation(libs.room.testing)
                implementation(libs.kotlinx.coroutines.test)
            }
        }

        jvmMain {
            dependencies {
                implementation(libs.icu)
                implementation(libs.room.driver.bundled)
                implementation(libs.sqlidelight.jvm)
            }
        }

        jvmTest {
            dependencies {
                implementation(libs.koin.test)
                implementation(libs.truth)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.koin.android)
                implementation(libs.sqlidelight.android)
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
                implementation(libs.turbine)
                implementation(libs.androidx.test.rules)
                implementation(libs.androidx.test.junit)
                implementation(libs.androidx.test.truth)
                implementation(libs.androidx.test.espresso.intents)
            }
        }
    }
}

room {
    generateKotlin = true
    schemaDirectory(layout.projectDirectory.dir("schemas"))
}

sqldelight {
    databases {
        create("Database") {
            packageName = "dev.fobo66.valiutchik.core.db"
        }
    }
}

detekt {
    autoCorrect = true
}

dependencies {
    add("kspJvm", libs.room.compiler)
    add("kspAndroid", libs.room.compiler)
    detektPlugins(libs.detekt.rules.compose)
}
