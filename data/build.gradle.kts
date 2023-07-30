/*
 *    Copyright 2023 Andrey Mukamolov
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
  id("com.google.dagger.hilt.android")
  id("io.gitlab.arturbosch.detekt")
  id("de.mannodermaus.android-junit5")
  id("androidx.room")
}

android {
  compileSdk = 34

  defaultConfig {
    minSdk = AndroidVersion.VersionCodes.N
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
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
  kotlinOptions {
    jvmTarget = "17"
  }
  namespace = "fobo66.valiutchik.core"
}

room {
  schemaDirectory("$projectDir/schemas/")
}

kapt {
  arguments {
    arg("room.incremental", "true")
    arg("room.expandProjection", "true")
    arg("dagger.ignoreProvisionKeyWildcards", "ENABLED")
  }
}

detekt {
  autoCorrect = true
}

dependencies {
  implementation(project(":api"))
  implementation(androidx.annotations)
  implementation(androidx.viewmodel)
  implementation(di.core)
  kapt(di.compiler)
  implementation(libs.moshi)
  kapt(libs.moshi.codegen)
  implementation(libs.coroutines)
  implementation(database.runtime)
  implementation(database.ktx)
  kapt(database.compiler)
  implementation(androidx.datastore)
  implementation(libs.mapbox)
  implementation(libs.timber)
  coreLibraryDesugaring(libs.desugar)

  detektPlugins(detektRules.formatting)
  detektPlugins(detektRules.compose)

  testImplementation(testing.junit)
  testRuntimeOnly(testing.junit.engine)
  testImplementation(testing.mockk)
  testImplementation(testing.mockk.agent)
  testImplementation(database.testing)
  testImplementation(libs.coroutines.test)

  androidTestImplementation(libs.coroutines.test)
  androidTestImplementation(testing.turbine)
  androidTestImplementation(androidx.uitest.core)
  androidTestImplementation(androidx.uitest.runner)
  androidTestImplementation(androidx.uitest.rules)
  androidTestImplementation(androidx.uitest.junit)
  androidTestImplementation(androidx.uitest.espresso.intents)
}
