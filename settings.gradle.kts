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
      library("android", "com.android.tools.build:gradle:8.0.0-alpha07")
      library("benchmark", "androidx.benchmark:benchmark-gradle-plugin:1.1.0")
      library("license", "com.jaredsburrows:gradle-license-plugin:0.9.0")
      library("junit5", "de.mannodermaus.gradle.plugins:android-junit5:1.8.2.1")
    }

    register("libs") {
      version("kotlin", "1.7.20")
      version("moshi", "1.14.0")
      version("coroutines", "1.6.4")
      library("material", "com.google.android.material:material:1.8.0-alpha02")
      library("retrofit", "com.squareup.retrofit2:retrofit:2.9.0")
      library("leakcanary", "com.squareup.leakcanary:leakcanary-android:2.9.1")
      library("mapbox", "com.mapbox.search:mapbox-search-android:1.0.0-beta.38.1")
      library("timber", "com.jakewharton.timber:timber:5.0.1")
      library("collections", "org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.5")
      library("desugar", "com.android.tools:desugar_jdk_libs:2.0.0")
      library(
        "coroutines",
        "org.jetbrains.kotlinx",
        "kotlinx-coroutines-android"
      )
        .versionRef("coroutines")
      library(
        "coroutines.core",
        "org.jetbrains.kotlinx",
        "kotlinx-coroutines-core"
      )
        .versionRef("coroutines")
      library(
        "coroutines-test",
        "org.jetbrains.kotlinx",
        "kotlinx-coroutines-test"
      ).versionRef("coroutines")
      library(
        "moshi",
        "com.squareup.moshi",
        "moshi"
      ).versionRef("moshi")
      library(
        "moshi-codegen",
        "com.squareup.moshi",
        "moshi-kotlin-codegen"
      ).versionRef("moshi")
    }

    register("androidx") {
      version("lifecycle", "2.6.0-alpha03")
      version("uitest", "1.5.1")
      version("espresso", "3.5.0-rc01")
      version("benchmark", "1.2.0-alpha07")
      library("core", "androidx.core:core-ktx:1.9.0")
      library("annotations", "androidx.annotation:annotation:1.5.0")
      library("activity", "androidx.activity:activity-compose:1.7.0-alpha02")
      library("appstartup", "androidx.startup:startup-runtime:1.1.1")
      library("datastore", "androidx.datastore:datastore-preferences:1.0.0")
      library("preference", "androidx.preference:preference-ktx:1.2.0")
      library("navigation", "androidx.navigation:navigation-compose:2.6.0-alpha03")
      library("multidex", "androidx.multidex:multidex:2.0.1")
      library("splashscreen", "androidx.core:core-splashscreen:1.0.0")
      library("window", "androidx.window:window:1.1.0-alpha04")
      library("lifecycle", "androidx.lifecycle", "lifecycle-runtime-compose").versionRef(
        "lifecycle"
      )
      library("viewmodel", "androidx.lifecycle", "lifecycle-viewmodel-compose").versionRef(
        "lifecycle"
      )
      library("uitest.core", "androidx.test", "core-ktx").versionRef("uitest")
      library("uitest.runner", "androidx.test", "runner").versionRef("uitest")
      library("uitest.rules", "androidx.test", "rules").versionRef("uitest")
      library(
        "uitest.espresso",
        "androidx.test.espresso",
        "espresso-core"
      ).versionRef("espresso")
      library(
        "uitest.espresso.contrib",
        "androidx.test.espresso",
        "espresso-contrib"
      ).versionRef("espresso")
      library(
        "uitest.espresso.intents",
        "androidx.test.espresso",
        "espresso-intents"
      ).versionRef("espresso")

      library("uitest.junit", "androidx.test.ext:junit-ktx:1.1.4-rc01")
      library("uitest.automator", "androidx.test.uiautomator:uiautomator:2.3.0-alpha01")
      library("uitest.benchmark", "androidx.benchmark", "benchmark-junit4").versionRef("benchmark")
      library("uitest.macrobenchmark", "androidx.benchmark", "benchmark-macro-junit4").versionRef("benchmark")
    }

    register("okhttp") {
      version("okhttp", "5.0.0-alpha.10")
      library("bom", "com.squareup.okhttp3", "okhttp-bom").versionRef("okhttp")
      library("core", "com.squareup.okhttp3", "okhttp").withoutVersion()
      library("logging", "com.squareup.okhttp3", "logging-interceptor").withoutVersion()
    }

    register("compose") {
      version("compiler", "1.4.0-alpha01")
      version("compose", "1.4.0-alpha02")
      version("material", "1.1.0-alpha02")
      library("ui", "androidx.compose.ui", "ui").versionRef("compose")
      library("preview", "androidx.compose.ui", "ui-tooling-preview").versionRef("compose")
      library("tooling", "androidx.compose.ui", "ui-tooling").versionRef("compose")
      library("testing", "androidx.compose.ui", "ui-test-junit4").versionRef("compose")
      library("testing.manifest", "androidx.compose.ui", "ui-test-manifest").versionRef("compose")
      library("material", "androidx.compose.material3", "material3").versionRef("material")
      library("windowsize", "androidx.compose.material3", "material3-window-size-class").versionRef("material")
    }

    register("accompanist") {
      version("accompanist", "0.27.0")
      library(
        "swiperefresh",
        "com.google.accompanist",
        "accompanist-swiperefresh"
      ).versionRef("accompanist")
      library(
        "permissions",
        "com.google.accompanist",
        "accompanist-permissions"
      ).versionRef("accompanist")
      library(
        "systemuicontroller",
        "com.google.accompanist",
        "accompanist-systemuicontroller"
      ).versionRef("accompanist")
    }

    register("di") {
      version("hilt", "2.44")
      library("plugin", "com.google.dagger", "hilt-android-gradle-plugin").versionRef("hilt")
      library("core", "com.google.dagger", "hilt-android").versionRef("hilt")
      library("compiler", "com.google.dagger", "hilt-android-compiler").versionRef("hilt")
      library("navigation", "androidx.hilt:hilt-navigation-compose:1.0.0")
    }

    register("room") {
      version("room", "2.5.0-beta01")
      library("runtime", "androidx.room", "room-runtime").versionRef("room")
      library("ktx", "androidx.room", "room-ktx").versionRef("room")
      library("compiler", "androidx.room", "room-compiler").versionRef("room")
      library("testing", "androidx.room", "room-testing").versionRef("room")
    }

    register("testing") {
      version("kaspresso", "1.4.2")
      version("mockk", "1.13.2")
      library("junit", "org.junit.jupiter:junit-jupiter:5.9.1")
      library("junit4", "junit:junit:4.13.2")
      library("kakao", "io.github.kakaocup:compose:0.1.1")
      library("kaspresso", "com.kaspersky.android-components", "kaspresso").versionRef("kaspresso")
      library(
        "kaspresso.compose",
        "com.kaspersky.android-components",
        "kaspresso-compose-support"
      ).versionRef("kaspresso")
      library("turbine", "app.cash.turbine:turbine:0.12.1")
      library("truth", "com.google.truth:truth:1.1.3")
      library("hamcrest", "org.hamcrest:hamcrest-core:2.2")
      library("mockk", "io.mockk", "mockk").versionRef("mockk")
      library("mockk.agent", "io.mockk", "mockk-agent-jvm").versionRef("mockk")
    }

    register("detektRules") {
      library("formatting", "io.gitlab.arturbosch.detekt:detekt-formatting:1.21.0")
      library("compose", "com.twitter.compose.rules:detekt:0.0.24")
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
    id("com.android.application") version "8.0.0-alpha08" apply false
    id("com.android.library") version "8.0.0-alpha08" apply false
    id("com.android.test") version "8.0.0-alpha08" apply false
    kotlin("android") version "1.7.20" apply false
    kotlin("kapt") version "1.7.20" apply false
    id("com.jaredsburrows.license") version "0.9.0"
    id("com.google.dagger.hilt.android") version "2.44" apply false
    id("androidx.benchmark") version "1.2.0-alpha06" apply false
    id("de.mannodermaus.android-junit5") version "1.8.2.1"
    id("io.gitlab.arturbosch.detekt") version "1.21.0"
  }
}

rootProject.name = "Valiutchik"

include(":app", ":benchmark", ":data", ":macrobenchmark", ":domain", ":api")
