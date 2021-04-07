plugins {
  id("com.android.library")
  kotlin("android")
}

android {
  compileSdk = 30

  defaultConfig {
    minSdk = 18
    targetSdk = 30
    versionCode = 1
    versionName = "1.0"

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
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  kotlinOptions {
    jvmTarget = "1.8"
    useIR = true
  }
}

dependencies {
  implementation("androidx.annotation:annotation:1.2.0")
  implementation("javax.inject:javax.inject:1")
  testImplementation("junit:junit:4.13.2")
}