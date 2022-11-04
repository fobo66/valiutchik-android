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
  id("com.google.dagger.hilt.android")
  id("io.gitlab.arturbosch.detekt")
  id("de.mannodermaus.android-junit5")
}

android {
  namespace = "fobo66.valiutchik.domain"
  compileSdk = AndroidVersion.VersionCodes.TIRAMISU

  defaultConfig {
    minSdk = AndroidVersion.VersionCodes.N

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    consumerProguardFiles("consumer-rules.pro")
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
    isCoreLibraryDesugaringEnabled = true
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
  kotlinOptions {
    jvmTarget = "11"
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
  api(project(":data"))
  implementation(androidx.annotations)
  implementation(libs.coroutines.core)
  implementation(di.core)
  kapt(di.compiler)
  implementation(libs.timber)
  coreLibraryDesugaring(libs.desugar)

  detektPlugins(detektRules.formatting)
  detektPlugins(detektRules.compose)

  testImplementation(testing.junit)
  testImplementation(libs.coroutines.test)

  androidTestImplementation(androidx.uitest.runner)
}
