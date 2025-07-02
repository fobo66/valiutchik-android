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
    alias(libs.plugins.android.library)
    kotlin("android")
    alias(libs.plugins.detekt)
    alias(libs.plugins.kotlinter)
    alias(libs.plugins.junit)
}

android {
    namespace = "fobo66.valiutchik.domain"
    compileSdk = AndroidVersion.VersionCodes.BAKLAVA

    defaultConfig {
        minSdk = AndroidVersion.VersionCodes.R

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
        freeCompilerArgs = listOf("-XXLanguage:+PropertyParamAnnotationDefaultTargetMode")
    }
}

detekt {
    autoCorrect = true
}

dependencies {
    api(project(":data"))
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.collection)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.datetime)
    implementation(platform(libs.koin.bom))
    implementation(platform(libs.compose.bom))
    implementation(libs.koin.core)
    implementation(libs.napier)
    compileOnly(libs.compose.stable.marker)

    detektPlugins(libs.detekt.rules.formatting)
    detektPlugins(libs.detekt.rules.compose)

    testApi(project(":domain-testing"))
    testApi(project(":data-testing"))
    testImplementation(libs.junit.api)
    testRuntimeOnly(libs.junit.engine)
    testImplementation(platform(libs.koin.bom))
    testImplementation(libs.koin.test)
    testImplementation(libs.ktor.client)
    testImplementation(libs.turbine)
    testImplementation(libs.truth)
    testImplementation(libs.kotlinx.coroutines.test)

    androidTestImplementation(libs.androidx.test.runner)
}
