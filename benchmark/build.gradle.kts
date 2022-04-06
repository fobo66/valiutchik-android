plugins {
  id("com.android.library")
  id("androidx.benchmark")
  kotlin("android")
}

android {
  compileSdk = com.android.sdklib.AndroidVersion.VersionCodes.S

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }

  kotlinOptions {
    jvmTarget = "11"
  }

  defaultConfig {
    minSdk = com.android.sdklib.AndroidVersion.VersionCodes.LOLLIPOP
    targetSdk = com.android.sdklib.AndroidVersion.VersionCodes.S
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
  androidTestImplementation("androidx.test:runner:1.4.0")
  androidTestImplementation("androidx.test.ext:junit:1.1.3")
  androidTestImplementation("junit:junit:4.13.2")
  androidTestImplementation("androidx.benchmark:benchmark-junit4:1.1.0-beta06")
}
