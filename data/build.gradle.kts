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
  id("com.android.library")
  kotlin("android")
  kotlin("kapt")
  id("dagger.hilt.android.plugin")
  id("io.gitlab.arturbosch.detekt")
  id("de.mannodermaus.android-junit5")
}

val hiltVersion = "2.44"

android {
  compileSdk = AndroidVersion.VersionCodes.TIRAMISU

  defaultConfig {
    minSdk = AndroidVersion.VersionCodes.LOLLIPOP
    version = 1

    multiDexEnabled = true
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    consumerProguardFiles("consumer-rules.pro")

    resValue(
      "string",
      "mapboxGeocoderAccessToken",
      loadSecret(rootProject, MAPBOX_TOKEN)
    )
  }

  buildFeatures {
    buildConfig = false
  }

  buildTypes {
    release {
      isMinifyEnabled = true
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
    register("benchmark") {
      initWith(getByName("release"))
      signingConfig = signingConfigs.getByName("debug")

      matchingFallbacks += listOf("release")
    }
  }
  compileOptions {
    isCoreLibraryDesugaringEnabled = true
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
  kotlinOptions {
    jvmTarget = "11"
  }
  namespace = "fobo66.valiutchik.core"
}

kapt {
  arguments {
    arg("room.schemaLocation", "$projectDir/schemas")
    arg("room.incremental", "true")
    arg("room.expandProjection", "true")
  }
}

detekt {
  autoCorrect = true
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
  // Target version of the generated JVM bytecode. It is used for type resolution.
  jvmTarget = "11"
}

dependencies {
  implementation(project(":api"))
  implementation(androidx.annotations)
  implementation(androidx.viewmodel)
  implementation("com.google.dagger:hilt-android:$hiltVersion")
  kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")
  implementation(libs.moshi)
  kapt(libs.moshi.codegen)
  implementation(libs.coroutines)
  implementation(room.ktx)
  kapt(room.compiler)
  implementation(androidx.preference)
  implementation(androidx.datastore)
  implementation(libs.mapbox)
  implementation(libs.timber)
  coreLibraryDesugaring(libs.desugar)

  detektPlugins(detektRules.formatting)
  detektPlugins(detektRules.compose)

  testImplementation(testing.junit)
  testImplementation(testing.mockk)
  testImplementation(testing.mockk.agent)
  testImplementation(room.testing)
  testImplementation(libs.coroutines.test)

  androidTestImplementation(libs.coroutines.test)
  androidTestImplementation(testing.turbine)
  androidTestImplementation(androidx.uitest.core)
  androidTestImplementation(androidx.uitest.runner)
  androidTestImplementation(androidx.uitest.rules)
  androidTestImplementation(androidx.uitest.junit)
  androidTestImplementation(androidx.uitest.espresso.intents)
}
