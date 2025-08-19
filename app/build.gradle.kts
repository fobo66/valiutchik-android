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
    alias(libs.plugins.android.app)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.hotreload)
    alias(libs.plugins.detekt)
    alias(libs.plugins.licenses)
    alias(libs.plugins.junit)
    alias(libs.plugins.kotlinter)
    alias(libs.plugins.baseline.profile)
}

android {
    signingConfigs {
        register("releaseSignConfig") {
            keyAlias = loadSecret(rootProject, KEY_ALIAS)
            keyPassword = loadSecret(rootProject, KEY_PASSWORD)
            storeFile = file(loadSecret(rootProject, STORE_FILE))
            storePassword = loadSecret(rootProject, STORE_PASSWORD)

            enableV3Signing = true
            enableV4Signing = true
        }
    }

    compileSdk = AndroidVersion.VersionCodes.BAKLAVA
    defaultConfig {
        applicationId = "fobo66.exchangecourcesbelarus"
        minSdk = AndroidVersion.VersionCodes.R
        targetSdk = AndroidVersion.VersionCodes.BAKLAVA
        versionCode = 25
        versionName = "1.15.1"
        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    androidResources {
        localeFilters += listOf("en", "be", "ru")
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
        }
        register("benchmark") {
            applicationIdSuffix = ".benchmark"
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks += listOf("release")
            isDebuggable = false
        }

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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            excludes += "META-INF/AL2.0"
            excludes += "META-INF/LGPL2.1"
        }
    }

    testOptions {
        animationsDisabled = true
        unitTests.isIncludeAndroidResources = false
    }

    namespace = "fobo66.exchangecourcesbelarus"
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

detekt {
    autoCorrect = true
}

composeCompiler {
    metricsDestination = project.layout.buildDirectory.dir("compose_metrics")
    reportsDestination = project.layout.buildDirectory.dir("compose_metrics")
}

licenseReport {
    generateCsvReport = false
    generateHtmlReport = false

    copyHtmlReportToAssets = false
    copyJsonReportToAssets = true
}

dependencies {
    api(project(":ui"))
    api(project(":presentation"))
    implementation(project(":widget"))

    // kotlin
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.collections)
    implementation(libs.kotlinx.datetime)

    // androidx
    implementation(libs.androidx.activity)
    implementation(libs.androidx.startup)
    implementation(libs.androidx.core)
    implementation(libs.androidx.splashscreen)
    implementation(libs.androidx.tracing)
    implementation(libs.androidx.window)
    implementation(libs.profileinstaller)
    implementation(libs.work.runtime)

    baselineProfile(project(":baselineprofile"))

    // compose
    val composeBom = platform(libs.compose.bom)
    implementation(composeBom)
    debugImplementation(composeBom)
    androidTestImplementation(composeBom)
    implementation(libs.compose.ui)
    implementation(libs.compose.xr)
    implementation(libs.compose.material)
    implementation(libs.compose.xr.material)
    implementation(libs.compose.ui.preview)
    implementation(libs.compose.material.windowsize)
    androidTestImplementation(libs.compose.ui.testing)
    debugImplementation(libs.compose.ui.testing.manifest)
    debugImplementation(libs.compose.ui.tooling)

    // lifecycle
    implementation(libs.androidx.lifecycle.compose)
    implementation(libs.androidx.lifecycle.viewmodel)

    implementation(platform(libs.koin.bom))
    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    implementation(libs.koin.viewmodel)
    implementation(libs.koin.work)

    implementation(libs.napier)

    // leakcanary
    debugImplementation(libs.leakcanary)

    detektPlugins(libs.detekt.rules.formatting)
    detektPlugins(libs.detekt.rules.compose)

    // tests
    testApi(project(":domain-testing"))
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.junit.api)
    testRuntimeOnly(libs.junit.engine)
    testImplementation(libs.turbine)
    testImplementation(libs.truth)

    androidTestApi(project(":domain-testing"))
    androidTestImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.turbine)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.test.espresso.contrib)
    androidTestImplementation(libs.androidx.test.espresso.intents)
    androidTestImplementation(libs.androidx.test.espresso.accessibility)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.truth)
    androidTestImplementation(libs.work.testing)
}
