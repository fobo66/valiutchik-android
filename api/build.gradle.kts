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

plugins {
    alias(libs.plugins.android.library)
    kotlin("android")
    kotlin("plugin.serialization")
    alias(libs.plugins.detekt)
    alias(libs.plugins.kotlinter)
    alias(libs.plugins.junit)
}

android {
    namespace = "fobo66.valiutchik.api"
    compileSdk = AndroidVersion.ApiBaseExtension.BAKLAVA.api

    defaultConfig {
        minSdk = AndroidVersion.VersionCodes.UPSIDE_DOWN_CAKE

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        resValue(
            "string",
            "apiUsername",
            loadSecret(rootProject, API_USERNAME)
        )

        resValue(
            "string",
            "apiPassword",
            loadSecret(rootProject, API_PASSWORD)
        )

        resValue(
            "string",
            "geocoderApiKey",
            loadSecret(rootProject, GEOCODER_TOKEN)
        )
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
    kotlinOptions {
        jvmTarget = "17"
    }
}

detekt {
    autoCorrect = true
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.androidx.collection)

    implementation(platform(libs.koin.bom))
    implementation(libs.koin.android)

    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp.core)
    debugImplementation(libs.ktor.logging)
    implementation(libs.ktor.client)
    implementation(libs.ktor.auth)
    implementation(libs.ktor.content)
    implementation(libs.ktor.encoding)
    implementation(libs.ktor.serialization)
    implementation(libs.ktor.serialization.xml)
    implementation(libs.kotlinx.serialization)

    implementation(libs.napier)

    detektPlugins(libs.detekt.rules.formatting)
    detektPlugins(libs.detekt.rules.compose)

    testImplementation(libs.junit.api)
    testImplementation(platform(libs.koin.bom))
    testImplementation(libs.koin.test)
    testRuntimeOnly(libs.junit.engine)

    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.androidx.test.junit)
}
