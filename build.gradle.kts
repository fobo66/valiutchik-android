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
  alias(androidx.plugins.application) apply false
  alias(androidx.plugins.library) apply false
  alias(androidx.plugins.test) apply false
  kotlin("android") version libs.versions.kotlin apply false
  alias(libs.plugins.licenses) apply false
  alias(libs.plugins.ksp) apply false
  alias(di.plugins.plugin) apply false
  alias(androidx.plugins.benchmark) apply false
  alias(testing.plugins.junit) apply false
  alias(detektRules.plugins.detekt) apply false
  alias(database.plugins.plugin) apply false
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
