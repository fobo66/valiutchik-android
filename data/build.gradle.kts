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
  kotlin("plugin.serialization")
  alias(libs.plugins.ksp)
  alias(di.plugins.plugin)
  alias(detektRules.plugins.detekt)
  alias(testing.plugins.junit)
  alias(database.plugins.plugin)
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
}

detekt {
  autoCorrect = true
}

dependencies {
  implementation(project(":api"))
  implementation(androidx.annotations)
  implementation(androidx.viewmodel)
  implementation(di.core)
  implementation(libs.serialization.json)
  ksp(di.compiler)
  implementation(libs.coroutines)
  implementation(database.runtime)
  implementation(database.ktx)
  ksp(database.compiler)
  implementation(androidx.datastore)
  implementation(libs.tomtom.geocoder)
  implementation(libs.tomtom.geocoder.online)
  implementation(libs.timber)

  detektPlugins(detektRules.formatting)
  detektPlugins(detektRules.compose)

  testImplementation(testing.junit)
  testRuntimeOnly(testing.junit.engine)
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
