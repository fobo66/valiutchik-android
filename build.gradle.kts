// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
  repositories {
    jcenter()
    google()
  }
  dependencies {
    classpath("com.android.tools.build:gradle:4.2.0-alpha16")
    classpath("com.google.gms:google-services:4.3.4")
    classpath("com.google.firebase:firebase-crashlytics-gradle:2.3.0")
    classpath(kotlin("gradle-plugin", version = "1.4.10"))
    classpath("androidx.benchmark:benchmark-gradle-plugin:1.0.0")
  }
}

allprojects {
  repositories {
    jcenter()
    google()
  }
}