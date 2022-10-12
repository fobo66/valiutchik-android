import com.android.sdklib.AndroidVersion

plugins {
  id("com.android.library")
  kotlin("android")
  kotlin("kapt")
  id("dagger.hilt.android.plugin")
  id("io.gitlab.arturbosch.detekt")
  id("de.mannodermaus.android-junit5")
}

val kotlinCoroutinesVersion = "1.6.4"
val junitVersion = "5.9.1"
val hiltVersion = "2.44"

android {
  namespace = "fobo66.valiutchik.domain"
  compileSdk = AndroidVersion.VersionCodes.TIRAMISU

  defaultConfig {
    minSdk = AndroidVersion.VersionCodes.LOLLIPOP

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    consumerProguardFiles("consumer-rules.pro")
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
      )
    }
  }
  compileOptions {
    isCoreLibraryDesugaringEnabled = true
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
  kotlinOptions {
    jvmTarget = "11"
  }
}

detekt {
  autoCorrect = true
}

dependencies {
  api(project(":data"))
  implementation("androidx.annotation:annotation:1.5.0")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutinesVersion")
  implementation("com.google.dagger:hilt-android:$hiltVersion")
  implementation("com.jakewharton.timber:timber:5.0.1")
  kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")
  coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.0")
  detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.21.0")
  detektPlugins("com.twitter.compose.rules:detekt:0.0.19")
  testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
  testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$kotlinCoroutinesVersion")
}
