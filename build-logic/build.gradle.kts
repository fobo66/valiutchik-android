/*
 *    Copyright 2026 Andrey Mukamolov
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

import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    mavenCentral()
    google()
}

dependencies {
    compileOnly(libs.android.gradle.api)
    implementation(gradleKotlinDsl())
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.android.library.multiplatform)
    implementation(libs.detekt)
    implementation(libs.kotlinter)
    implementation(libs.dotenv.kotlin)
    implementation(libs.kotlinpoet)
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.fromTarget("17")
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

gradlePlugin {
    plugins {
        register("androidConventions") {
            id = "buildlogic.android-conventions"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("secretsConventions") {
            id = "buildlogic.secrets-conventions"
            implementationClass = "SecretsConventionPlugin"
        }
    }
}
