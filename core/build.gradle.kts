import com.android.sdklib.AndroidVersion

plugins {
  id("com.android.library")
  kotlin("android")
  id("io.gitlab.arturbosch.detekt")
}

val kotlinCoroutinesVersion = "1.5.2"

android {
  compileSdk = AndroidVersion.VersionCodes.S

  defaultConfig {
    minSdk = AndroidVersion.VersionCodes.LOLLIPOP
    targetSdk = AndroidVersion.VersionCodes.S
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

detekt {
  autoCorrect = true
}

dependencies {
  implementation("androidx.annotation:annotation:1.3.0")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutinesVersion")
  implementation("javax.inject:javax.inject:1")
  coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")
  detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.19.0")
  testImplementation("junit:junit:4.13.2")
}
