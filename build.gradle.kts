// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
  repositories {
    jcenter()
    google()
  }
  dependencies {
    classpath("com.android.tools.build:gradle:7.0.0-alpha04")
    classpath(kotlin("gradle-plugin", version = "1.4.21"))
    classpath("androidx.benchmark:benchmark-gradle-plugin:1.0.0")
    classpath("com.google.dagger:hilt-android-gradle-plugin:2.30.1-alpha")
  }
}

plugins {
  id("io.gitlab.arturbosch.detekt").version("1.14.2")
}

allprojects {
  repositories {
    jcenter()
    google()
  }
}

tasks {
  withType<io.gitlab.arturbosch.detekt.Detekt> {
    // Target version of the generated JVM bytecode. It is used for type resolution.
    this.jvmTarget = "1.8"
  }
}