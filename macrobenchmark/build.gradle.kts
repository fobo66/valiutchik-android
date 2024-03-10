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

import com.android.build.api.dsl.ManagedVirtualDevice
import com.android.sdklib.AndroidVersion

plugins {
  alias(libs.plugins.android.test)
  kotlin("android")
  alias(libs.plugins.baseline.profile)
}

android {
  namespace = "dev.fobo66.valiutchik.macrobenchmark"
  compileSdk = AndroidVersion.VersionCodes.UPSIDE_DOWN_CAKE

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    isCoreLibraryDesugaringEnabled = true
  }

  kotlinOptions {
    jvmTarget = "17"
  }

  defaultConfig {
    minSdk = AndroidVersion.VersionCodes.P
    targetSdk = AndroidVersion.VersionCodes.UPSIDE_DOWN_CAKE

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildTypes {
    // This benchmark buildType is used for benchmarking, and should function like your
    // release build (for example, with minification on). It"s signed with a debug key
    // for easy local/CI testing.
    create("benchmark") {
      isDebuggable = true
      signingConfig = getByName("debug").signingConfig
      matchingFallbacks += listOf("release")
    }
  }

  testOptions.managedDevices.devices {
    create<ManagedVirtualDevice>("pixel6Api34") {
      device = "Pixel 6"
      apiLevel = AndroidVersion.VersionCodes.UPSIDE_DOWN_CAKE
      systemImageSource = "google"
    }
  }

  targetProjectPath = ":app"
  experimentalProperties["android.experimental.self-instrumenting"] = true
}

baselineProfile {
  managedDevices += "pixel6Api34"
  useConnectedDevices = false
}

dependencies {
  implementation(libs.androidx.test.junit)
  implementation(libs.androidx.test.espresso.core)
  implementation(libs.androidx.test.uiautomator)
  implementation(libs.androidx.benchmark.macro)
  coreLibraryDesugaring(libs.desugar)
}

androidComponents {
  beforeVariants(selector().all()) {
    it.enable = it.buildType == "benchmark"
  }
}
