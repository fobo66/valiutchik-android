import com.android.sdklib.AndroidVersion

plugins {
  id("com.android.library")
  kotlin("android")
  kotlin("kapt")
  id("io.gitlab.arturbosch.detekt")
  id("de.mannodermaus.android-junit5")
}

val kotlinCoroutinesVersion = "1.6.4"
val junitVersion = "5.9.1"
val moshiVersion = "1.14.0"

android {
  compileSdk = 33

  defaultConfig {
    minSdk = AndroidVersion.VersionCodes.LOLLIPOP
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
      isMinifyEnabled = true
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
    register("benchmark") {
      initWith(getByName("release"))
      signingConfig = signingConfigs.getByName("debug")

      matchingFallbacks += listOf("release")
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
  implementation("androidx.annotation:annotation:1.5.0")
  implementation("com.squareup.moshi:moshi:$moshiVersion")
  kapt("com.squareup.moshi:moshi-kotlin-codegen:$moshiVersion")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutinesVersion")
  implementation("javax.inject:javax.inject:1")
  coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.0")
  detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.21.0")
  detektPlugins("com.twitter.compose.rules:detekt:0.0.14")
  testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
}
