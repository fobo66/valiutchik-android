/*
 *    Copyright 2022 Andrey Mukamolov
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
  id("com.android.application")
  kotlin("android")
  kotlin("kapt")
  id("dagger.hilt.android.plugin")
  id("io.gitlab.arturbosch.detekt")
  id("com.jaredsburrows.license")
  id("de.mannodermaus.android-junit5")
}

val composeVersion = "1.3.2"
val composeUiVersion = "1.3.0"
val accompanistVersion = "0.27.0"
val kotlinCoroutinesVersion = "1.6.4"
val hiltVersion = "2.44"
val activityVersion = "1.6.1"
val navVersion = "2.5.3"
val lifecycleVersion = "2.6.0-alpha03"
val junitVersion = "5.9.1"
val turbineVersion = "0.12.0"
val kaspressoVersion = "1.4.2"

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

  compileSdk = AndroidVersion.VersionCodes.TIRAMISU
  defaultConfig {
    applicationId = "fobo66.exchangecourcesbelarus"
    minSdk = AndroidVersion.VersionCodes.LOLLIPOP
    targetSdk = AndroidVersion.VersionCodes.TIRAMISU
    versionCode = 18
    versionName = "1.12.1"
    multiDexEnabled = true
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    isCoreLibraryDesugaringEnabled = true

    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }

  kotlinOptions {
    jvmTarget = "11"

    freeCompilerArgs = freeCompilerArgs + "-opt-in=kotlin.RequiresOptIn"
  }

  buildFeatures {
    buildConfig = false
    compose = true
  }

  packagingOptions {
    resources {
      excludes += "META-INF/AL2.0"
      excludes += "META-INF/LGPL2.1"
    }
  }

  testOptions {
    animationsDisabled = true
    unitTests.isIncludeAndroidResources = true
  }

  composeOptions {
    kotlinCompilerExtensionVersion = composeVersion
  }
  namespace = "fobo66.exchangecourcesbelarus"
}

detekt {
  autoCorrect = true
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
  // Target version of the generated JVM bytecode. It is used for type resolution.
  jvmTarget = "11"
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
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutinesVersion")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$kotlinCoroutinesVersion")
  implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.5")

  // androidx
  implementation("androidx.annotation:annotation:1.5.0")
  implementation("androidx.activity:activity-compose:$activityVersion")
  implementation("androidx.core:core-ktx:1.9.0")
  implementation("com.google.android.material:material:1.8.0-alpha02")
  implementation("androidx.core:core-splashscreen:1.0.0")

  // compose
  implementation("androidx.compose.ui:ui:$composeUiVersion")
  implementation("androidx.compose.material3:material3:1.0.0")
  implementation("androidx.compose.ui:ui-tooling-preview:$composeUiVersion")
  implementation("androidx.activity:activity-compose:$activityVersion")
  androidTestImplementation("androidx.compose.ui:ui-test-junit4:$composeUiVersion")
  debugImplementation("androidx.compose.ui:ui-tooling:$composeUiVersion")

  implementation("com.google.accompanist:accompanist-swiperefresh:$accompanistVersion")
  implementation("com.google.accompanist:accompanist-permissions:$accompanistVersion")
  implementation("com.google.accompanist:accompanist-systemuicontroller:$accompanistVersion")

  // lifecycle
  implementation("androidx.lifecycle:lifecycle-runtime-compose:$lifecycleVersion")
  implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")

  // nav
  implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
  implementation("androidx.navigation:navigation-ui-ktx:$navVersion")
  implementation("androidx.navigation:navigation-compose:$navVersion")
  implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

  // multidex
  implementation("androidx.multidex:multidex:2.0.1")

  // timber
  implementation(libs.timber)

  // leakcanary
  debugImplementation(libs.leakcanary)

  coreLibraryDesugaring(libs.desugar)

  detektPlugins(detektRules.formatting)
  detektPlugins(detektRules.compose)

  // tests
  testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
  testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$kotlinCoroutinesVersion")
  testImplementation("app.cash.turbine:turbine:$turbineVersion")
  testImplementation("com.google.truth:truth:1.1.3")
  androidTestImplementation("androidx.test:core:1.5.0-rc01")
  androidTestImplementation(
    "org.jetbrains.kotlinx:kotlinx-coroutines-test:$kotlinCoroutinesVersion"
  )
  androidTestImplementation("androidx.arch.core:core-testing:2.1.0")
  androidTestImplementation("com.kaspersky.android-components:kaspresso:$kaspressoVersion")
  androidTestImplementation(
    "com.kaspersky.android-components:kaspresso-compose-support:$kaspressoVersion"
  )
  androidTestImplementation("io.github.kakaocup:compose:0.1.1")
  androidTestImplementation("app.cash.turbine:turbine:$turbineVersion")
  androidTestImplementation("androidx.test:runner:1.5.0-rc01")
  androidTestImplementation("androidx.test:rules:1.5.0-rc01")
  androidTestImplementation("androidx.test.espresso:espresso-contrib:3.5.0-rc01")
  androidTestImplementation("androidx.test.espresso:espresso-intents:3.5.0-rc01")
  androidTestImplementation("org.hamcrest:hamcrest-core:2.2")
  androidTestImplementation("androidx.test.ext:junit-ktx:1.1.4-rc01")
  androidTestImplementation("androidx.compose.ui:ui-test-junit4:$composeUiVersion")
  debugImplementation("androidx.compose.ui:ui-test-manifest:$composeUiVersion")

  // dagger
  implementation("com.google.dagger:hilt-android:$hiltVersion")
  kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")
}
