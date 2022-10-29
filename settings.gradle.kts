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


dependencyResolutionManagement {
  versionCatalogs {
    register("buildscriptPlugins") {
      library("android", "com.android.tools.build:gradle:8.0.0-alpha06")
    }

    register("libs") {
      version("kotlin", "1.7.20")
      version("coroutines", "1.6.4")
      library("coil", "io.coil-kt:coil:2.2.2")
      library("material", "com.google.android.material:material:1.8.0-alpha02")
      library("timber", "com.jakewharton.timber:timber:5.0.1")
      library("desugar", "com.android.tools:desugar_jdk_libs:2.0.0")
      library(
        "coroutines",
        "org.jetbrains.kotlinx",
        "kotlinx-coroutines-android"
      )
        .versionRef("coroutines")
      library(
        "coroutines-test",
        "org.jetbrains.kotlinx",
        "kotlinx-coroutines-test"
      ).versionRef("coroutines")
    }

    register("androidx") {
      version("lifecycle", "2.5.1")
      library("core", "androidx.core:core-ktx:1.9.0")
      library("activity", "androidx.activity:activity-ktx:1.6.1")
      library("appstartup", "androidx.startup:startup-runtime:1.1.1")
      library("constraint", "androidx.constraintlayout:constraintlayout:2.1.4")
      library("datastore", "androidx.datastore:datastore-preferences:1.0.0")
      library("lifecycle", "androidx.lifecycle", "lifecycle-runtime-ktx").versionRef(
        "lifecycle"
      )
      library("viewmodel", "androidx.lifecycle", "lifecycle-viewmodel-ktx").versionRef(
        "lifecycle"
      )
      library("uitest.core", "androidx.test:core-ktx:1.4.0")
      library("uitest.junit", "androidx.test.ext:junit-ktx:1.1.3")
      library("uitest.runner", "androidx.test:runner:1.4.0")
    }

    register("room") {
      version("room", "2.5.0-beta01")
      library("runtime", "androidx.room", "room-runtime").versionRef("room")
      library("ktx", "androidx.room", "room-ktx").versionRef("room")
      library("compiler", "androidx.room", "room-compiler").versionRef("room")
    }
  }
}

pluginManagement {
  repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
  }
  plugins {
    id("io.gitlab.arturbosch.detekt") version "1.21.0"
  }
}

rootProject.name = "Valiutchik"

include(":app", ":benchmark", ":data", ":macrobenchmark", ":domain", ":api")
