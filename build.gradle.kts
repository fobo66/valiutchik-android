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

plugins {
  id("com.android.application") version "8.1.0-alpha04" apply false
  id("com.android.library") version "8.1.0-alpha04" apply false
  id("com.android.test") version "8.1.0-alpha05" apply false
  kotlin("android") version "1.8.10" apply false
  kotlin("kapt") version "1.8.10" apply false
  id("com.jaredsburrows.license") version "0.9.0" apply false
  id("com.google.dagger.hilt.android") version "2.45" apply false
  id("androidx.benchmark") version "1.2.0-alpha09" apply false
  id("de.mannodermaus.android-junit5") version "1.8.2.1" apply false
  id("io.gitlab.arturbosch.detekt") version "1.22.0" apply false
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

tasks.register<Delete>("clean") {
  delete(rootProject.buildDir)
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
  // Target version of the generated JVM bytecode. It is used for type resolution.
  jvmTarget = "11"
}
