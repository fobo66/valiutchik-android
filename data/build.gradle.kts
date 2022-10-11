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
val moshiVersion = "1.14.0"
val roomVersion = "2.4.3"
val hiltVersion = "2.44"
val mockkVersion = "1.13.2"

android {
  compileSdk = AndroidVersion.VersionCodes.TIRAMISU

  defaultConfig {
    minSdk = AndroidVersion.VersionCodes.LOLLIPOP
    version = 1

    multiDexEnabled = true
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    consumerProguardFiles("consumer-rules.pro")

    resValue(
      "string",
      "mapboxGeocoderAccessToken",
      loadSecret(rootProject, MAPBOX_TOKEN)
    )
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

kapt {
  arguments {
    arg("room.schemaLocation", "$projectDir/schemas")
    arg("room.incremental", "true")
    arg("room.expandProjection", "true")
  }
}

detekt {
  autoCorrect = true
}

dependencies {
  api(project(":api"))
  implementation("androidx.annotation:annotation:1.5.0")
  implementation("com.google.dagger:hilt-android:$hiltVersion")
  kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")
  implementation("com.squareup.moshi:moshi:$moshiVersion")
  kapt("com.squareup.moshi:moshi-kotlin-codegen:$moshiVersion")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$kotlinCoroutinesVersion")
  implementation("androidx.room:room-runtime:$roomVersion")
  implementation("androidx.room:room-ktx:$roomVersion")
  kapt("androidx.room:room-compiler:$roomVersion")
  implementation("androidx.preference:preference-ktx:1.2.0")
  implementation("androidx.datastore:datastore-preferences:1.0.0")
  implementation("com.mapbox.search:mapbox-search-android:1.0.0-beta.37")
  implementation("com.jakewharton.timber:timber:5.0.1")
  coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.0")
  detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.21.0")
  detektPlugins("com.twitter.compose.rules:detekt:0.0.19")
  testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
  testImplementation("io.mockk:mockk:$mockkVersion")
  testImplementation("io.mockk:mockk-agent-jvm:$mockkVersion")
  testImplementation("androidx.room:room-testing:$roomVersion")
  testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$kotlinCoroutinesVersion")
}
