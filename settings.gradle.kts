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


dependencyResolutionManagement {
  versionCatalogs {
    register("libs") {
      version("kotlin", "1.9.10")
      version("moshi", "1.15.0")
      version("coroutines", "1.7.3")
      plugin("licenses", "com.jaredsburrows.license").version("0.9.3")
      plugin("ksp", "com.google.devtools.ksp").version("1.9.10-1.0.13")
      library("material", "com.google.android.material:material:1.9.0")
      library("retrofit", "com.squareup.retrofit2:retrofit:2.9.0")
      library("leakcanary", "com.squareup.leakcanary:leakcanary-android:2.12")
      library("mapbox", "com.mapbox.search:mapbox-search-android:1.0.0-rc.7")
      library("timber", "com.jakewharton.timber:timber:5.0.1")
      library("collections", "org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.5")
      library("desugar", "com.android.tools:desugar_jdk_libs:2.0.3")
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
      version("plugin", "8.3.0-alpha03")
      version("lifecycle", "2.7.0-alpha01")
      version("espresso", "3.5.1")
      version("benchmark", "1.2.0-beta02")
      plugin("application", "com.android.application").versionRef("plugin")
      plugin("library", "com.android.library").versionRef("plugin")
      plugin("test", "com.android.test").versionRef("plugin")
      plugin("benchmark", "androidx.benchmark").versionRef("benchmark")
      library("core", "androidx.core:core-ktx:1.12.0-rc01")
      library("annotations", "androidx.annotation:annotation:1.6.0")
      library("activity", "androidx.activity:activity-compose:1.8.0-alpha07")
      library("appstartup", "androidx.startup:startup-runtime:1.2.0-alpha02")
      library("datastore", "androidx.datastore:datastore-preferences:1.1.0-alpha05")
      library("navigation", "androidx.navigation:navigation-compose:2.7.2")
      library("multidex", "androidx.multidex:multidex:2.0.1")
      library("splashscreen", "androidx.core:core-splashscreen:1.0.1")
      library("tracing", "androidx.tracing:tracing:1.3.0-alpha02")
      library("window", "androidx.window:window:1.2.0-beta01")
      library("lifecycle", "androidx.lifecycle", "lifecycle-runtime-compose").versionRef(
        "lifecycle"
      )
      library("viewmodel", "androidx.lifecycle", "lifecycle-viewmodel-compose").versionRef(
        "lifecycle"
      )
      library("uitest.core", "androidx.test:core-ktx:1.5.0")
      library("uitest.runner", "androidx.test:runner:1.5.2")
      library("uitest.rules", "androidx.test:rules:1.5.0")
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
      library(
        "uitest.espresso.accessibility",
        "androidx.test.espresso",
        "espresso-accessibility"
      ).versionRef("espresso")

      library("uitest.junit", "androidx.test.ext:junit-ktx:1.1.5")
      library("uitest.automator", "androidx.test.uiautomator:uiautomator:2.3.0-alpha03")
      library("uitest.benchmark", "androidx.benchmark", "benchmark-junit4").versionRef("benchmark")
      library(
        "uitest.macrobenchmark",
        "androidx.benchmark",
        "benchmark-macro-junit4"
      ).versionRef("benchmark")
    }

    register("okhttp") {
      version("okhttp", "5.0.0-alpha.11")
      library("bom", "com.squareup.okhttp3", "okhttp-bom").versionRef("okhttp")
      library("core", "com.squareup.okhttp3", "okhttp").withoutVersion()
      library("logging", "com.squareup.okhttp3", "logging-interceptor").withoutVersion()
    }

    register("compose") {
      version("compiler", "1.5.3")
      version("compose", "1.6.0-alpha05")
      version("material", "1.2.0-alpha08")
      library("ui", "androidx.compose.ui", "ui").versionRef("compose")
      library("preview", "androidx.compose.ui", "ui-tooling-preview").versionRef("compose")
      library("tooling", "androidx.compose.ui", "ui-tooling").versionRef("compose")
      library("testing", "androidx.compose.ui", "ui-test-junit4").versionRef("compose")
      library("testing.manifest", "androidx.compose.ui", "ui-test-manifest").versionRef("compose")
      library("material", "androidx.compose.material3", "material3").versionRef("material")
      library("windowsize", "androidx.compose.material3", "material3-window-size-class").versionRef(
        "material"
      )
    }

    register("widget") {
      version("glance", "1.0.0")
      version("tools", "0.2.2")
      library("glance", "androidx.glance", "glance").versionRef("glance")
      library("glance.appwidget", "androidx.glance", "glance-appwidget").versionRef("glance")
      library(
        "glance.preview",
        "com.google.android.glance.tools",
        "appwidget-host"
      ).versionRef("tools")
    }

    register("accompanist") {
      version("accompanist", "0.33.0-alpha")
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
      version("hilt", "2.48")
      plugin("plugin", "com.google.dagger.hilt.android").versionRef("hilt")
      library("core", "com.google.dagger", "hilt-android").versionRef("hilt")
      library("compiler", "com.google.dagger", "hilt-android-compiler").versionRef("hilt")
      library("navigation", "androidx.hilt:hilt-navigation-compose:1.0.0")
    }

    register("database") {
      version("room", "2.6.0-alpha03")
      plugin("plugin", "androidx.room").versionRef("room")
      library("runtime", "androidx.room", "room-runtime").versionRef("room")
      library("ktx", "androidx.room", "room-ktx").versionRef("room")
      library("compiler", "androidx.room", "room-compiler").versionRef("room")
      library("testing", "androidx.room", "room-testing").versionRef("room")
    }

    register("testing") {
      version("kaspresso", "1.5.3")
      version("mockk", "1.13.7")
      version("junit", "5.10.0")
      plugin("junit", "de.mannodermaus.android-junit5").version("1.9.3.0")
      library("junit", "org.junit.jupiter", "junit-jupiter-api").versionRef("junit")
      library("junit.engine", "org.junit.jupiter", "junit-jupiter-engine").versionRef("junit")
      library("junit4", "junit:junit:4.13.2")
      library("kakao", "io.github.kakaocup:compose:0.2.3")
      library("kaspresso", "com.kaspersky.android-components", "kaspresso").versionRef("kaspresso")
      library(
        "kaspresso.compose",
        "com.kaspersky.android-components",
        "kaspresso-compose-support"
      ).versionRef("kaspresso")
      library("turbine", "app.cash.turbine:turbine:1.0.0")
      library("truth", "com.google.truth:truth:1.1.5")
      library("hamcrest", "org.hamcrest:hamcrest-core:2.2")
      library("mockk", "io.mockk", "mockk").versionRef("mockk")
      library("mockk.agent", "io.mockk", "mockk-agent-jvm").versionRef("mockk")
    }

    register("detektRules") {
      version("detekt", "1.23.1")
      plugin("detekt", "io.gitlab.arturbosch.detekt").versionRef("detekt")
      library("formatting", "io.gitlab.arturbosch.detekt", "detekt-formatting").versionRef("detekt")
      library("compose", "io.nlopez.compose.rules:detekt:0.2.2")
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
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
  }
}

rootProject.name = "Valiutchik"

include(":app", ":benchmark", ":data", ":macrobenchmark", ":domain", ":api")
