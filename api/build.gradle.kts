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
import io.gitlab.arturbosch.detekt.Detekt

plugins {
  id("com.android.library")
  kotlin("android")
  kotlin("kapt")
  id("dagger.hilt.android.plugin")
  id("io.gitlab.arturbosch.detekt")
  id("de.mannodermaus.android-junit5")
}

val kotlinCoroutinesVersion = "1.6.4"
val junitVersion = "5.9.1"
val hiltVersion = "2.44"

android {
  namespace = "fobo66.valiutchik.api"
  compileSdk = AndroidVersion.VersionCodes.TIRAMISU

  defaultConfig {
    minSdk = AndroidVersion.VersionCodes.LOLLIPOP

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

tasks.withType<Detekt>().configureEach {
  // Target version of the generated JVM bytecode. It is used for type resolution.
  jvmTarget = "11"
}


dependencies {
  implementation(libs.coroutines.core)
  implementation("com.google.dagger:hilt-android:$hiltVersion")
  kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")
  implementation(platform("com.squareup.okhttp3:okhttp-bom:5.0.0-alpha.10"))
  implementation("com.squareup.okhttp3:okhttp")
  debugImplementation("com.squareup.okhttp3:logging-interceptor")
  implementation(libs.retrofit)
  implementation(libs.timber)
  coreLibraryDesugaring(libs.desugar)
  detektPlugins(detektRules.formatting)
  detektPlugins(detektRules.compose)
  testImplementation(testing.junit)
  androidTestImplementation(androidx.uitest.core)
  androidTestImplementation(androidx.uitest.runner)
  androidTestImplementation(androidx.uitest.rules)
  androidTestImplementation(androidx.uitest.junit)
}
