// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
  repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
  }
  dependencies {
    classpath("com.android.tools.build:gradle:7.1.0-alpha01")
    classpath(kotlin("gradle-plugin", version = "1.5.0"))
    classpath("androidx.benchmark:benchmark-gradle-plugin:1.0.0")
    classpath("com.google.dagger:hilt-android-gradle-plugin:2.36")
    classpath("com.google.android.gms:oss-licenses-plugin:0.10.4")
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
