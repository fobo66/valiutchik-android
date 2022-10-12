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

import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
  repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
  }
  dependencies {
    classpath("com.android.tools.build:gradle:8.0.0-alpha03")
    classpath(kotlin("gradle-plugin", version = "1.7.20"))
    classpath("androidx.benchmark:benchmark-gradle-plugin:1.1.0")
    classpath("com.google.dagger:hilt-android-gradle-plugin:2.44")
    classpath("com.jaredsburrows:gradle-license-plugin:0.9.0")
    classpath("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.21.0")
    classpath("de.mannodermaus.gradle.plugins:android-junit5:1.8.2.1")
  }
}

allprojects {
  repositories {
    mavenCentral()
    google()
    maven {
      url = uri("https://api.mapbox.com/downloads/v2/releases/maven")
      authentication {
        create<BasicAuthentication>("basic")
      }
      credentials {
        username = "mapbox"
        password = loadSecret(rootProject, MAPBOX_REPO_TOKEN)
      }
    }
  }
}

subprojects {
  tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
      if (project.findProperty("valiutchik.enableComposeCompilerReports") == "true") {
        freeCompilerArgs = freeCompilerArgs + listOf(
          "-P",
          "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=" +
            project.buildDir.absolutePath + "/compose_metrics"
        )
        freeCompilerArgs = freeCompilerArgs + listOf(
          "-P",
          "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=" +
            project.buildDir.absolutePath + "/compose_metrics"
        )
      }
    }
  }
}

tasks.register<Delete>("clean") {
  delete(rootProject.buildDir)
}

tasks.withType<Detekt>().configureEach {
  // Target version of the generated JVM bytecode. It is used for type resolution.
  jvmTarget = "11"
}
