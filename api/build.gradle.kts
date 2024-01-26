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
  alias(androidx.plugins.library)
  kotlin("android")
  alias(libs.plugins.ksp)
  alias(di.plugins.plugin)
  alias(detektRules.plugins.detekt)
  alias(testing.plugins.junit)
}

android {
  namespace = "fobo66.valiutchik.api"
  compileSdk = AndroidVersion.VersionCodes.UPSIDE_DOWN_CAKE

  defaultConfig {
    minSdk = AndroidVersion.VersionCodes.O

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
      isMinifyEnabled = true
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
  kotlinOptions {
    jvmTarget = "17"
  }
}

detekt {
  autoCorrect = true
}


dependencies {
  implementation(libs.coroutines.core)

  implementation(di.core)
  ksp(di.compiler)

  implementation(platform(okhttp.bom))
  implementation(okhttp.core)
  debugImplementation(ktor.logging)
  implementation(ktor.client)
  implementation(ktor.auth)

  implementation(libs.timber)

  detektPlugins(detektRules.formatting)
  detektPlugins(detektRules.compose)

  testImplementation(testing.junit)
  testRuntimeOnly(testing.junit.engine)

  androidTestImplementation(androidx.uitest.core)
  androidTestImplementation(androidx.uitest.runner)
  androidTestImplementation(androidx.uitest.rules)
  androidTestImplementation(androidx.uitest.junit)
}
