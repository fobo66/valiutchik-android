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
            target.configureLibrary()
        }

        if (hasTestPlugin) {
            target.configureBaselineProfile()
        }

        if (hasAppPlugin) {
            target.configureApplication()
        }

        if (hasAppPlugin || hasLibraryPlugin || hasTestPlugin) {
            target.configureKotlin()
        }
    }

    private fun CommonExtension.configureCommon() {
        compileSdk {
            version = release(COMPILE_ANDROID_SDK_VERSION) {
                minorApiLevel = 1
            }
        }
        defaultConfig.minSdk {
            version = release(MIN_ANDROID_SDK_VERSION)
        }
        compileOptions.sourceCompatibility = JavaVersion.VERSION_17
        compileOptions.targetCompatibility = JavaVersion.VERSION_17

        packaging.resources.excludes.addAll(listOf("META-INF/AL2.0", "META-INF/LGPL2.1"))
    }

    private fun Project.configureKotlin() =
        extensions.configure<KotlinAndroidProjectExtension>("kotlin") {
            compilerOptions {
                jvmTarget.set(JvmTarget.fromTarget(TARGET_JVM_VERSION))
            }
        }

    private fun Project.configureApplication() =
        extensions.configure<ApplicationExtension>("android") {
            configureCommon()

            signingConfigs {
                register("releaseSignConfig") {
                    val env = loadEnv()
                    keyAlias = env[ENV_VAR_KEY_ALIAS]
                    keyPassword = env[ENV_VAR_KEY_PASSWORD]
                    storeFile = file(env[ENV_VAR_STORE_FILE])
                    storePassword = env[ENV_VAR_STORE_PASSWORD]

                    enableV1Signing = true
                    enableV2Signing = true
                    enableV3Signing = true
                    enableV4Signing = true
                }
            }

            defaultConfig {
                targetSdk {
                    version = release(TARGET_ANDROID_SDK_VERSION)
                }

                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }

            buildTypes {
                release {
                    isMinifyEnabled = true
                    isShrinkResources = true
                    proguardFiles(
                        getDefaultProguardFile("proguard-android-optimize.txt"),
                        "proguard-rules.pro"
                    )
                    signingConfig = signingConfigs.getByName("releaseSignConfig")
                }
            }
        }

    private fun Project.configureLibrary() = extensions.configure<LibraryExtension>("android") {
        configureCommon()

        defaultConfig {
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            consumerProguardFiles("consumer-rules.pro")
        }
    }

    private fun Project.configureBaselineProfile() =
        extensions.configure<TestExtension>("android") {
            configureCommon()

            defaultConfig {
                targetSdk {
                    version = release(TARGET_ANDROID_SDK_VERSION)
                }

                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }
        }
}
