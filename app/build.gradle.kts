plugins {
  id("com.android.application")
  id("com.google.firebase.crashlytics")
  kotlin("android")
  kotlin("kapt")
}
val kotlinVersion = "1.4.10"
val kotlinCoroutinesVersion = "1.3.9"
val okhttpVersion = "4.9.0"
val daggerVersion = "2.30.1"
val roomVersion = "2.2.5"
val lifecycleVersion = "2.2.0"

android {
  signingConfigs {
    create("releaseSignConfig") {
      val keystoreProperties = loadProperties(rootProject.file("keystore.properties"))

      keyAlias = keystoreProperties["keyAlias"].toString()
      keyPassword = keystoreProperties["keyPassword"].toString()
      storeFile = file(keystoreProperties["storeFile"].toString())
      storePassword = keystoreProperties["storePassword"].toString()

      enableV3Signing = true
      enableV4Signing = true
    }
  }

  compileSdk = 30
  defaultConfig {
    applicationId = "fobo66.exchangecourcesbelarus"
    minSdk = 16
    targetSdk = 30
    versionCode = 15
    versionName = "1.10.3"
    multiDexEnabled = true
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    javaCompileOptions {
      annotationProcessorOptions {
        arguments.putAll(
          mapOf(
            "room.schemaLocation" to "$projectDir/schemas",
            "room.incremental" to "true",
            "room.expandProjection" to "true"
          )
        )
      }
    }
  }
  buildTypes {
    getByName("release") {
      isMinifyEnabled = true
      isShrinkResources = true
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
      signingConfig = signingConfigs.getByName("releaseSignConfig")
    }
    getByName("debug") {
      isMinifyEnabled = false
      firebaseCrashlytics {
        mappingFileUploadEnabled = false
      }
    }
  }
  compileOptions {
    isCoreLibraryDesugaringEnabled = true

    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }

  kotlinOptions {
    jvmTarget = JavaVersion.VERSION_1_8.toString()
  }

  buildFeatures {
    viewBinding = true
  }

  testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {
  implementation(project(":core"))

  // kotlin
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutinesVersion")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$kotlinCoroutinesVersion")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:$kotlinCoroutinesVersion")

  // androidx
  implementation("androidx.annotation:annotation:1.1.0")
  implementation("androidx.activity:activity-ktx:1.2.0-beta02")
  implementation("androidx.recyclerview:recyclerview:1.1.0")
  implementation("androidx.fragment:fragment-ktx:1.3.0-beta02")
  implementation("androidx.collection:collection-ktx:1.1.0")
  implementation("androidx.core:core-ktx:1.5.0-alpha05")
  implementation("androidx.constraintlayout:constraintlayout:2.0.4")
  implementation("com.google.android.material:material:1.3.0-alpha04")
  implementation("androidx.preference:preference-ktx:1.1.1")
  implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

  // lifecycle
  implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
  implementation("androidx.lifecycle:lifecycle-extensions:$lifecycleVersion")
  implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")
  implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")

  // location
  implementation("com.google.android.gms:play-services-location:17.1.0")
  implementation("com.mapbox.mapboxsdk:mapbox-sdk-services:5.7.0")

  // firebase
  implementation(platform("com.google.firebase:firebase-bom:26.0.0"))
  implementation("com.google.firebase:firebase-analytics-ktx")
  implementation("com.google.firebase:firebase-crashlytics")

  // room
  implementation("androidx.room:room-runtime:$roomVersion")
  implementation("androidx.room:room-ktx:$roomVersion")
  kapt("androidx.room:room-compiler:$roomVersion")

  // http
  implementation("com.squareup.okhttp3:okhttp:$okhttpVersion")

  // multidex
  implementation("androidx.multidex:multidex:2.0.1")

  // timber
  implementation("com.jakewharton.timber:timber:4.7.1")

  // insets
  implementation("dev.chrisbanes:insetter-ktx:0.3.1")

  // leakcanary
  debugImplementation("com.squareup.leakcanary:leakcanary-android:2.5")

  coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.1")

  // tests
  testImplementation("junit:junit:4.13.1")
  testImplementation("androidx.test:core:1.3.0")
  testImplementation("io.mockk:mockk:1.10.2")
  testImplementation("com.squareup.okhttp3:mockwebserver:$okhttpVersion")
  testImplementation("androidx.room:room-testing:$roomVersion")
  testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$kotlinCoroutinesVersion")
  androidTestImplementation(
    "org.jetbrains.kotlinx:kotlinx-coroutines-test:$kotlinCoroutinesVersion"
  )
  androidTestImplementation("androidx.arch.core:core-testing:2.1.0")
  androidTestImplementation("app.cash.turbine:turbine:0.2.1")
  androidTestImplementation("androidx.test:runner:1.3.0")

  // dagger
  implementation("com.google.dagger:dagger:$daggerVersion")
  kapt("com.google.dagger:dagger-compiler:$daggerVersion")
}

apply(plugin = "com.google.gms.google-services")