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
  namespace = "fobo66.valiutchik.api"
  compileSdk = 33

  defaultConfig {
    minSdk = 21

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

dependencies {
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutinesVersion")
  implementation("com.google.dagger:hilt-android:$hiltVersion")
  kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")
  implementation(platform("com.squareup.okhttp3:okhttp-bom:5.0.0-alpha.10"))
  implementation("com.squareup.okhttp3:okhttp")
  debugImplementation("com.squareup.okhttp3:logging-interceptor")
  implementation("com.squareup.retrofit2:retrofit:2.9.0")
  coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.0")
  detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.21.0")
  detektPlugins("com.twitter.compose.rules:detekt:0.0.18")
  testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
}
