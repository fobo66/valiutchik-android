plugins {
  id("com.android.library")
  kotlin("android")
}

val kotlinCoroutinesVersion = "1.5.0"

android {
  compileSdkPreview = "android-S"

  defaultConfig {
    minSdk = 21
    targetSdkPreview = "S"
    version = 1

    multiDexEnabled = true
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    consumerProguardFiles("consumer-rules.pro")
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
  implementation("androidx.annotation:annotation:1.3.0-alpha01")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutinesVersion")
  implementation("javax.inject:javax.inject:1")
  coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")
  testImplementation("junit:junit:4.13.2")
}
