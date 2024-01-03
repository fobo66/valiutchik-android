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
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

  repositories {
    google()
    mavenCentral()
    maven {
      url = uri("https://repositories.tomtom.com/artifactory/maven")
    }
  }
  versionCatalogs {
    register("libs") {
      version("kotlin", "1.9.21")
      version("moshi", "1.15.0")
      version("coroutines", "1.7.3")
      version("tomtom", "0.42.0")
      plugin("licenses", "com.jaredsburrows.license").version("0.9.3")
      plugin("ksp", "com.google.devtools.ksp").version("1.9.21-1.0.16")
      library("material", "com.google.android.material:material:1.11.0")
      library("leakcanary", "com.squareup.leakcanary:leakcanary-android:2.13")
      library("tomtom-geocoder", "com.tomtom.sdk.search", "reverse-geocoder").versionRef("tomtom")
      library(
        "tomtom-geocoder-online",
        "com.tomtom.sdk.search",
        "reverse-geocoder-online"
      ).versionRef("tomtom")
      library("timber", "com.jakewharton.timber:timber:5.0.1")
      library("collections", "org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.7")
      library("desugar", "com.android.tools:desugar_jdk_libs:2.0.4")
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
      version("plugin", "8.4.0-alpha01")
      version("lifecycle", "2.7.0-rc02")
      version("espresso", "3.5.1")
      version("benchmark", "1.2.2")
      plugin("application", "com.android.application").versionRef("plugin")
      plugin("library", "com.android.library").versionRef("plugin")
      plugin("test", "com.android.test").versionRef("plugin")
      plugin("benchmark", "androidx.benchmark").versionRef("benchmark")
      library("core", "androidx.core:core-ktx:1.12.0")
      library("annotations", "androidx.annotation:annotation:1.7.1")
      library("activity", "androidx.activity:activity-compose:1.8.2")
      library("appstartup", "androidx.startup:startup-runtime:1.2.0-alpha02")
      library("datastore", "androidx.datastore:datastore-preferences:1.1.0-alpha07")
      library("navigation", "androidx.navigation:navigation-compose:2.7.6")
      library("multidex", "androidx.multidex:multidex:2.0.1")
      library("splashscreen", "androidx.core:core-splashscreen:1.0.1")
      library("tracing", "androidx.tracing:tracing:1.3.0-alpha02")
      library("window", "androidx.window:window:1.2.0")
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
      library("uitest.automator", "androidx.test.uiautomator:uiautomator:2.3.0-beta01")
      library("uitest.benchmark", "androidx.benchmark", "benchmark-junit4").versionRef("benchmark")
      library(
        "uitest.macrobenchmark",
        "androidx.benchmark",
        "benchmark-macro-junit4"
      ).versionRef("benchmark")
    }

    register("okhttp") {
      version("okhttp", "5.0.0-alpha.12")
      library("bom", "com.squareup.okhttp3", "okhttp-bom").versionRef("okhttp")
      library("core", "com.squareup.okhttp3", "okhttp").withoutVersion()
    }
    register("ktor") {
      version("ktor", "2.3.7")
      library("client", "io.ktor", "ktor-client-okhttp").versionRef("ktor")
      library("client-mock", "io.ktor", "ktor-client-mock").versionRef("ktor")
      library("auth", "io.ktor", "ktor-client-auth").versionRef("ktor")
      library("logging", "io.ktor", "ktor-client-logging").versionRef("ktor")
    }

    register("compose") {
      version("compiler", "1.5.6")
      version("compose", "1.6.0-beta03")
      version("material", "1.2.0-beta01")
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
      library("glance.material", "androidx.glance", "glance-material3").versionRef("glance")
      library("glance.appwidget", "androidx.glance", "glance-appwidget").versionRef("glance")
      library(
        "glance.preview",
        "com.google.android.glance.tools",
        "appwidget-host"
      ).versionRef("tools")
    }

    register("accompanist") {
      version("accompanist", "0.33.2-alpha")
      library(
        "permissions",
        "com.google.accompanist",
        "accompanist-permissions"
      ).versionRef("accompanist")
    }

    register("di") {
      version("hilt", "2.50")
      plugin("plugin", "com.google.dagger.hilt.android").versionRef("hilt")
      library("core", "com.google.dagger", "hilt-android").versionRef("hilt")
      library("compiler", "com.google.dagger", "hilt-android-compiler").versionRef("hilt")
      library("navigation", "androidx.hilt:hilt-navigation-compose:1.1.0")
    }

    register("database") {
      version("room", "2.6.1")
      plugin("plugin", "androidx.room").versionRef("room")
      library("runtime", "androidx.room", "room-runtime").versionRef("room")
      library("ktx", "androidx.room", "room-ktx").versionRef("room")
      library("compiler", "androidx.room", "room-compiler").versionRef("room")
      library("testing", "androidx.room", "room-testing").versionRef("room")
    }

    register("testing") {
      version("kaspresso", "1.5.3")
      version("junit", "5.10.1")
      plugin("junit", "de.mannodermaus.android-junit5").version("1.10.0.0")
      library("junit", "org.junit.jupiter", "junit-jupiter-api").versionRef("junit")
      library("junit.engine", "org.junit.jupiter", "junit-jupiter-engine").versionRef("junit")
      library("junit4", "junit:junit:4.13.2")
      library("kakao", "io.github.kakaocup:compose:0.3.0")
      library("kaspresso", "com.kaspersky.android-components", "kaspresso").versionRef("kaspresso")
      library(
        "kaspresso.compose",
        "com.kaspersky.android-components",
        "kaspresso-compose-support"
      ).versionRef("kaspresso")
      library("turbine", "app.cash.turbine:turbine:1.0.0")
      library("truth", "com.google.truth:truth:1.2.0")
      library("hamcrest", "org.hamcrest:hamcrest-core:2.2")
    }

    register("detektRules") {
      version("detekt", "1.23.4")
      plugin("detekt", "io.gitlab.arturbosch.detekt").versionRef("detekt")
      library("formatting", "io.gitlab.arturbosch.detekt", "detekt-formatting").versionRef("detekt")
      library("compose", "io.nlopez.compose.rules:detekt:0.3.8")
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
