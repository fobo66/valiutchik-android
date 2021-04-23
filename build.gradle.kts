// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
  repositories {
    mavenCentral()
    google()
  }
  dependencies {
    classpath("com.android.tools.build:gradle:7.0.0-alpha14")
    classpath(kotlin("gradle-plugin", version = "1.4.32"))
    classpath("androidx.benchmark:benchmark-gradle-plugin:1.0.0")
    classpath("com.google.dagger:hilt-android-gradle-plugin:2.35")
  }
}

plugins {
  id("io.gitlab.arturbosch.detekt").version("1.16.0")
}

allprojects {
  repositories {
    mavenCentral()
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
