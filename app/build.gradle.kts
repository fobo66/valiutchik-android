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

plugins {
    alias(libs.plugins.android.app)
    alias(libs.plugins.compose)
    alias(libs.plugins.licenses)
    alias(libs.plugins.baseline.profile)
    alias(libs.plugins.metro)
    id("buildlogic.common-conventions")
    id("buildlogic.android-conventions")
}

android {

    defaultConfig {
        applicationId = "fobo66.exchangecourcesbelarus"

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
    }

    buildFeatures {
        compose = true
    }

    testOptions {
        animationsDisabled = true
        unitTests.isIncludeAndroidResources = false
    }

    namespace = "fobo66.exchangecourcesbelarus"
}

detekt {
    config.setFrom(rootProject.file("config/detekt/compose.yml"))
}

composeCompiler {
    metricsDestination = project.layout.buildDirectory.dir("compose_metrics")
    reportsDestination = project.layout.buildDirectory.dir("compose_metrics")
}

aboutLibraries {
    export {
        outputFile = file("src/main/assets/open_source_licenses.json")
        variant = "release"
    }
}

dependencies {
    implementation(project(":ui"))
    implementation(project(":widget"))
    implementation(project(":domain:api"))
    implementation(project(":domain:wiring"))
    implementation(project(":presentation:wiring"))
    implementation(project(":api:wiring"))
    implementation(project(":data:wiring"))

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
    implementation(libs.circuit.runtime)
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

    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    implementation(libs.koin.viewmodel)
    implementation(libs.koin.work)
    implementation(libs.metrox.android)
    implementation(libs.metrox.viewmodel.compose)

    implementation(libs.napier)

    // leakcanary
    debugImplementation(libs.leakcanary)

    detektPlugins(libs.detekt.rules.compose)

    androidTestImplementation(project(":domain:testing"))
    androidTestImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.kotlin.test)
    androidTestImplementation(libs.turbine)
    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.truth)
    androidTestImplementation(libs.work.testing)
}
