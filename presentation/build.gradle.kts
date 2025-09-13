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
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library.multiplatform)
    alias(libs.plugins.android.lint)
    alias(libs.plugins.detekt)
    alias(libs.plugins.kotlinter)
}

kotlin {
    jvm {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_17

            freeCompilerArgs.add("-Xcontext-parameters")
        }
    }

    androidLibrary {
        namespace = "dev.fobo66.valiutchik.presentation"
        compileSdk = AndroidVersion.VersionCodes.BAKLAVA
        minSdk = AndroidVersion.VersionCodes.R

        compilations.configureEach {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget = JvmTarget.JVM_17
                    freeCompilerArgs.add("-Xcontext-parameters")
                }
            }
        }
    }

    // Source set declarations.
    // Declaring a target automatically creates a source set with the same name. By default, the
    // Kotlin Gradle Plugin creates additional source sets that depend on each other, since it is
    // common to share sources between related targets.
    // See: https://kotlinlang.org/docs/multiplatform-hierarchy.html
    sourceSets {
        commonMain {
            dependencies {
                api(project(":domain"))
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.collections)
                implementation(libs.kotlinx.datetime)
                implementation(libs.androidx.lifecycle.viewmodel)
                implementation(project.dependencies.platform(libs.koin.bom))
                implementation(project.dependencies.platform(libs.compose.bom))
                implementation(libs.koin.core)
                implementation(libs.koin.viewmodel)
                implementation(libs.napier)
                compileOnly(libs.compose.stable.marker)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
                api(project(":data-testing"))
                api(project(":data"))
                api(project(":domain-testing"))
                implementation(project.dependencies.platform(libs.ktor.bom))
                implementation(project.dependencies.platform(libs.koin.bom))
                implementation(libs.koin.test)
                implementation(libs.ktor.client)
                implementation(libs.turbine)
                implementation(libs.kotlinx.coroutines.test)
            }
        }
    }
}

dependencies {
    detektPlugins(libs.detekt.rules.compose)
}
