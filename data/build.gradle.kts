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
  alias(libs.plugins.android.library)
  kotlin("android")
  kotlin("plugin.serialization")
  alias(libs.plugins.ksp)
  alias(libs.plugins.hilt)
  alias(libs.plugins.detekt)
  alias(libs.plugins.junit)
  alias(libs.plugins.room)
}

android {
  compileSdk = AndroidVersion.VersionCodes.UPSIDE_DOWN_CAKE

  defaultConfig {
    minSdk = AndroidVersion.VersionCodes.O

    multiDexEnabled = true
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    consumerProguardFiles("consumer-rules.pro")

    resValue(
      "string",
      "geocoderAccessToken",
      loadSecret(rootProject, GEOCODER_TOKEN)
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
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    isCoreLibraryDesugaringEnabled = true
  }
  kotlinOptions {
    jvmTarget = "17"
  }
  packaging {
    jniLibs.pickFirsts.add("lib/**/libc++_shared.so")
  }
  namespace = "fobo66.valiutchik.core"
}

room {
  schemaDirectory("$projectDir/schemas/")
}

ksp {
  arg("room.incremental", "true")
  arg("room.expandProjection", "true")
  arg("room.generateKotlin", "true")
}

detekt {
  autoCorrect = true
}

dependencies {
  implementation(project(":api"))
  implementation(libs.androidx.annotation)
  implementation(libs.hilt.core)
  implementation(libs.kotlinx.serialization)
  ksp(libs.hilt.compiler)
  implementation(libs.kotlinx.coroutines.android)
  implementation(libs.kotlinx.datetime)
  implementation(libs.room.runtime)
  implementation(libs.room.ktx)
  ksp(libs.room.compiler)
  implementation(libs.androidx.datastore)
  implementation(libs.tomtom.geocoder)
  implementation(libs.tomtom.geocoder.online)
  implementation(libs.timber)
  implementation(libs.napier)

  coreLibraryDesugaring(libs.desugar)

  detektPlugins(libs.detekt.rules.formatting)
  detektPlugins(libs.detekt.rules.compose)

  testImplementation(libs.junit.api)
  testRuntimeOnly(libs.junit.engine)
  testImplementation(libs.room.testing)
  testImplementation(libs.kotlinx.coroutines.test)

  androidTestImplementation(libs.kotlinx.coroutines.test)
  androidTestImplementation(libs.turbine)
  androidTestImplementation(libs.androidx.test.rules)
  androidTestImplementation(libs.androidx.test.junit)
  androidTestImplementation(libs.androidx.test.espresso.intents)
}
