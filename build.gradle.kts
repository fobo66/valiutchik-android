// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
  repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
  }
  dependencies {
    classpath("com.android.tools.build:gradle:7.1.0-alpha02")
    classpath(kotlin("gradle-plugin", version = "1.5.10"))
    classpath("androidx.benchmark:benchmark-gradle-plugin:1.0.0")
    classpath("com.google.dagger:hilt-android-gradle-plugin:2.36")
    classpath("com.jaredsburrows:gradle-license-plugin:0.8.90")
  }
}

allprojects {
  repositories {
    mavenCentral()
    jcenter()
    google()
    maven(url = "https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
  }
}
