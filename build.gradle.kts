import java.net.URI

// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
  repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
  }
  dependencies {
    classpath("com.android.tools.build:gradle:7.3.0-alpha07")
    classpath(kotlin("gradle-plugin", version = "1.6.10"))
    classpath("androidx.benchmark:benchmark-gradle-plugin:1.0.0")
    classpath("com.google.dagger:hilt-android-gradle-plugin:2.41")
    classpath("com.jaredsburrows:gradle-license-plugin:0.8.91")
    classpath("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.19.0")
  }
}

allprojects {
  repositories {
    mavenCentral()
    google()
    maven(url = "https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
    maven {
      url = URI.create("https://api.mapbox.com/downloads/v2/releases/maven")
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
