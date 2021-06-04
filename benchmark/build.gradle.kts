plugins {
  id("com.android.library")
  id("androidx.benchmark")
  kotlin("android")
}

android {
  compileSdkPreview = "android-S"

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }

  kotlinOptions {
    jvmTarget = "1.8"
  }

  defaultConfig {
    minSdk = 21
    targetSdkPreview = "S"
    version = 1

    testInstrumentationRunner = "androidx.benchmark.junit4.AndroidBenchmarkRunner"
  }

  testBuildType = "release"
  buildTypes {
    debug {
      // Since debuggable can"t be modified by gradle for library modules,
      // it must be done in a manifest - see src/androidTest/AndroidManifest.xml
      isMinifyEnabled = true
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "benchmark-proguard-rules.pro"
      )
    }
    release {
      isDefault = true
    }
  }
}

dependencies {
  implementation(project(":core"))
  androidTestImplementation("androidx.test:runner:1.4.0-beta01")
  androidTestImplementation("androidx.test.ext:junit:1.1.2")
  androidTestImplementation("junit:junit:4.13.2")
  androidTestImplementation("androidx.benchmark:benchmark-junit4:1.1.0-alpha02")
}
