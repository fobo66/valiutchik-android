buildscript {
  repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
  }
  dependencies {
    classpath("com.android.tools.build:gradle:7.4.0-beta01")
    classpath(kotlin("gradle-plugin", version = "1.7.10"))
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

tasks.register("clean", Delete::class) {
  delete(rootProject.buildDir)
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
  // Target version of the generated JVM bytecode. It is used for type resolution.
  jvmTarget = "11"
}
