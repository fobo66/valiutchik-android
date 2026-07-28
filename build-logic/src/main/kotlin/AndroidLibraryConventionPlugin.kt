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

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.dsl.TestExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val hasLibraryPlugin = target.pluginManager.hasPlugin("com.android.library")
        val hasAppPlugin = target.pluginManager.hasPlugin("com.android.application")
        val hasTestPlugin = target.pluginManager.hasPlugin("com.android.test")
        if (hasLibraryPlugin) {
            target.extensions.configure<LibraryExtension>("android") {
                setupSdk()

                defaultConfig {
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                    consumerProguardFiles("consumer-rules.pro")
                }
            }
        }

        if (hasTestPlugin) {
            target.extensions.configure<TestExtension>("android") {
                setupSdk()

                defaultConfig {
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }
            }
        }

        if (hasAppPlugin) {
            target.extensions.configure<ApplicationExtension>("android") {
                setupSdk()

                defaultConfig {
                    targetSdk {
                        version = release(37)
                    }

                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }
            }
        }

        if (hasAppPlugin || hasLibraryPlugin || hasTestPlugin) {
            target.extensions.configure<KotlinAndroidProjectExtension>("kotlin") {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_17)
                }
            }
        }
    }

    fun CommonExtension.setupSdk() {
        compileSdk {
            version = release(37) {
                minorApiLevel = 1
            }
        }
        defaultConfig.minSdk {
            version = release(30)
        }
        compileOptions.sourceCompatibility = JavaVersion.VERSION_17
        compileOptions.targetCompatibility = JavaVersion.VERSION_17
    }
}
