import com.android.sdklib.AndroidVersion

plugins {
  id("com.android.library")
  kotlin("android")
  id("io.gitlab.arturbosch.detekt")
  id("de.mannodermaus.android-junit5")
}

val kotlinCoroutinesVersion = "1.6.4"
val junitVersion = "5.9.1"

android {
  compileSdk = 33

  defaultConfig {
    minSdk = AndroidVersion.VersionCodes.LOLLIPOP
    targetSdk = AndroidVersion.VersionCodes.S
    version = 1

    multiDexEnabled = true
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    consumerProguardFiles("consumer-rules.pro")
  }

  buildFeatures {
    buildConfig = false
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
  namespace = "fobo66.valiutchik.core"
}

detekt {
  autoCorrect = true
}

dependencies {
  implementation("androidx.annotation:annotation:1.4.0")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutinesVersion")
  implementation("javax.inject:javax.inject:1")
  coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.0")
  detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.21.0")
  testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
}
