// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
  repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
  }
  dependencies {
    classpath("com.android.tools.build:gradle:7.0.0-alpha15")
    classpath(kotlin("gradle-plugin", version = "1.4.32"))
    classpath("androidx.benchmark:benchmark-gradle-plugin:1.0.0")
    classpath("com.google.dagger:hilt-android-gradle-plugin:2.35.1")
  }
}

allprojects {
  repositories {
    mavenCentral()
    jcenter()
    google()
  }
}