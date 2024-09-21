/*
 *    Copyright 2024 Andrey Mukamolov
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
  alias(libs.plugins.android.app)
  kotlin("android")
  kotlin("plugin.compose")
  alias(libs.plugins.detekt)
  alias(libs.plugins.licenses)
  alias(libs.plugins.junit)
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

  compileSdk = AndroidVersion.VersionCodes.VANILLA_ICE_CREAM
  defaultConfig {
    applicationId = "fobo66.exchangecourcesbelarus"
    minSdk = AndroidVersion.VersionCodes.O
    targetSdk = AndroidVersion.VersionCodes.VANILLA_ICE_CREAM
    versionCode = 23
    versionName = "1.14.2"
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
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
      signingConfig = signingConfigs.getByName("releaseSignConfig")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }

  kotlinOptions {
    jvmTarget = "17"

    freeCompilerArgs = freeCompilerArgs + "-opt-in=kotlin.RequiresOptIn"
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
    unitTests.isIncludeAndroidResources = true
  }

  namespace = "fobo66.exchangecourcesbelarus"
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
  api(project(":domain"))

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
  implementation(libs.compose.material)
  implementation(libs.compose.ui.preview)
  implementation(libs.compose.material.windowsize)
  implementation(libs.compose.material.adaptive)
  implementation(libs.compose.material.adaptive.layout)
  implementation(libs.compose.material.adaptive.navigation)
  androidTestImplementation(libs.compose.ui.testing)
  debugImplementation(libs.compose.ui.testing.manifest)
  debugImplementation(libs.compose.ui.tooling)

  implementation(libs.accompanist.permissions)

  implementation(libs.androidx.glance)
  implementation(libs.androidx.glance.appwidget)
  implementation(libs.androidx.glance.material)
  debugImplementation(libs.androidx.glance.preview)
  debugImplementation(libs.androidx.glance.appwidget.preview)

  // lifecycle
  implementation(libs.androidx.lifecycle.compose)
  implementation(libs.androidx.lifecycle.viewmodel)

  implementation(platform(libs.koin.bom))
  implementation(libs.koin.android)
  implementation(libs.koin.compose)
  implementation(libs.koin.navigation)
  implementation(libs.koin.viewmodel)
  implementation(libs.koin.work)

  implementation(libs.napier)

  // leakcanary
  debugImplementation(libs.leakcanary)


  detektPlugins(libs.detekt.rules.formatting)
  detektPlugins(libs.detekt.rules.compose)

  // tests
  testImplementation(libs.kotlinx.coroutines.test)
  testImplementation(libs.junit.api)
  testRuntimeOnly(libs.junit.engine)
  testImplementation(libs.turbine)
  testImplementation(libs.truth)

  androidTestImplementation(libs.kotlinx.coroutines.test)
  androidTestImplementation(libs.turbine)
  androidTestImplementation(libs.androidx.test.rules)
  androidTestImplementation(libs.androidx.test.espresso.core)
  androidTestImplementation(libs.androidx.test.espresso.contrib)
  androidTestImplementation(libs.androidx.test.espresso.intents)
  androidTestImplementation(libs.androidx.test.espresso.accessibility)
  androidTestImplementation(libs.androidx.test.junit)
  androidTestImplementation(libs.work.testing)
}
