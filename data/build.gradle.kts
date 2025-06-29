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
    kotlin("plugin.serialization")
    alias(libs.plugins.ksp)
    alias(libs.plugins.detekt)
    alias(libs.plugins.junit)
    alias(libs.plugins.room)
}

android {
    compileSdk = AndroidVersion.VersionCodes.BAKLAVA

    defaultConfig {
        minSdk = AndroidVersion.VersionCodes.R

        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        buildConfig = false
    }

    buildTypes {
        release {
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        register("benchmark") {
            initWith(getByName("release"))
            signingConfig = signingConfigs.getByName("debug")

            matchingFallbacks += listOf("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    packaging {
        jniLibs.pickFirsts.add("lib/**/libc++_shared.so")
    }
    namespace = "fobo66.valiutchik.core"
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

room {
    generateKotlin = true
    schemaDirectory("$projectDir/schemas/")
}

detekt {
    autoCorrect = true
}

dependencies {
    api(project(":api"))
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.collection)
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.android)
    implementation(libs.kotlinx.serialization)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.datetime)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
    implementation(libs.androidx.datastore)
    implementation(libs.napier)
    implementation(libs.uri)

    detektPlugins(libs.detekt.rules.formatting)
    detektPlugins(libs.detekt.rules.compose)

    testApi(project(":data-testing"))
    testImplementation(libs.junit.api)
    testRuntimeOnly(libs.junit.engine)
    testImplementation(platform(libs.koin.bom))
    testImplementation(libs.koin.test)
    testImplementation(libs.room.testing)
    testImplementation(libs.ktor.client)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.truth)

    androidTestApi(project(":data-testing"))
    androidTestImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.truth)
    androidTestImplementation(libs.turbine)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.truth)
    androidTestImplementation(libs.androidx.test.espresso.intents)
}
