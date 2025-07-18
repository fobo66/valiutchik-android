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
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose)
    alias(libs.plugins.detekt)
    alias(libs.plugins.junit)
    alias(libs.plugins.kotlinter)
}

android {
    namespace = "dev.fobo66.valiutchik.android.widget"
    compileSdk = AndroidVersion.VersionCodes.BAKLAVA

    defaultConfig {
        minSdk = AndroidVersion.VersionCodes.R

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        compose = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
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
    }
}

detekt {
    autoCorrect = true
}

dependencies {
    val composeBom = platform(libs.compose.bom)
    implementation(composeBom)
    debugImplementation(composeBom)
    androidTestImplementation(composeBom)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.preview)
    androidTestImplementation(libs.compose.ui.testing)
    debugImplementation(libs.compose.ui.testing.manifest)
    debugImplementation(libs.compose.ui.tooling)
    api(libs.androidx.glance)
    api(libs.androidx.glance.appwidget)
    api(libs.androidx.glance.material)
    api(libs.androidx.glance.preview)
    api(libs.androidx.glance.appwidget.preview)
    implementation(libs.kotlinx.collections)
    detektPlugins(libs.detekt.rules.formatting)
    detektPlugins(libs.detekt.rules.compose)
    testImplementation(libs.junit.api)
    testRuntimeOnly(libs.junit.engine)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.junit)
}
