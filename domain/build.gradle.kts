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
    alias(libs.plugins.android.library.multiplatform)
    kotlin("multiplatform")
    alias(libs.plugins.detekt)
    alias(libs.plugins.kotlinter)
    alias(libs.plugins.junit)
}

kotlin {
    jvm {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_17
        }
    }

    androidLibrary {
        namespace = "fobo66.valiutchik.domain"
        compileSdk = AndroidVersion.VersionCodes.BAKLAVA

        minSdk = AndroidVersion.VersionCodes.R

        compilations.configureEach {
            compilerOptions.configure {
                jvmTarget = JvmTarget.JVM_17
            }
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                api(project(":data"))
                implementation(libs.androidx.annotation)
                implementation(libs.androidx.collection)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.datetime)
                implementation(project.dependencies.platform(libs.koin.bom))
                implementation(project.dependencies.platform(libs.compose.bom))
                implementation(libs.koin.core)
                implementation(libs.napier)
                compileOnly(libs.compose.stable.marker)
            }
        }

        commonTest {
            dependencies {
                implementation(kotlin("test"))
                api(project(":data-testing"))
                implementation(project.dependencies.platform(libs.koin.bom))
                implementation(libs.koin.test)
                implementation(libs.ktor.client)
                implementation(libs.turbine)
                implementation(libs.kotlinx.coroutines.test)
            }
        }
    }
}

detekt {
    autoCorrect = true
}

dependencies {
    detektPlugins(libs.detekt.rules.formatting)
    detektPlugins(libs.detekt.rules.compose)
}
