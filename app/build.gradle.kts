plugins {
  id("com.android.application")
  kotlin("android")
  kotlin("kapt")
  id("dagger.hilt.android.plugin")
}

val kotlinCoroutinesVersion = "1.4.3"
val okhttpVersion = "5.0.0-alpha.2"
val hiltVersion = "2.34.1-beta"
val roomVersion = "2.2.6"
val navVersion = "2.3.5"
val lifecycleVersion = "2.3.1"
val flowBindingVersion = "1.0.0"

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
    minSdk = 18
    targetSdk = 30
    versionCode = 16
    versionName = "1.11.1"
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
    debug {
      applicationIdSuffix = ".debug"
    }

    release {
      isMinifyEnabled = true
      isShrinkResources = true
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
      signingConfig = signingConfigs.getByName("releaseSignConfig")
    }
  }
  compileOptions {
    isCoreLibraryDesugaringEnabled = true

    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }

  kotlinOptions {
    jvmTarget = JavaVersion.VERSION_1_8.toString()
    useIR = true
  }

  buildFeatures {
    viewBinding = true
  }

  packagingOptions {
    resources {
      excludes += "META-INF/AL2.0"
      excludes += "META-INF/LGPL2.1"
    }
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
  implementation("androidx.annotation:annotation:1.2.0")
  implementation("androidx.activity:activity-ktx:1.3.0-alpha06")
  implementation("androidx.recyclerview:recyclerview:1.2.0")
  implementation("androidx.fragment:fragment-ktx:1.3.2")
  implementation("androidx.collection:collection-ktx:1.1.0")
  implementation("androidx.core:core-ktx:1.6.0-alpha02")
  implementation("androidx.constraintlayout:constraintlayout:2.0.4")
  implementation("com.google.android.material:material:1.3.0")
  implementation("androidx.preference:preference-ktx:1.1.1")
  implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

  // flowbinding

  implementation("io.github.reactivecircus.flowbinding:flowbinding-android:$flowBindingVersion")
  implementation("io.github.reactivecircus.flowbinding:flowbinding-swiperefreshlayout:$flowBindingVersion")
  implementation("io.github.reactivecircus.flowbinding:flowbinding-appcompat:$flowBindingVersion")
  implementation("io.github.reactivecircus.flowbinding:flowbinding-material:$flowBindingVersion")

  // lifecycle
  implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")

  // location
  implementation("com.google.android.gms:play-services-location:18.0.0")
  implementation("com.mapbox.mapboxsdk:mapbox-sdk-services:5.9.0-alpha.1")

  // room
  implementation("androidx.room:room-runtime:$roomVersion")
  implementation("androidx.room:room-ktx:$roomVersion")
  kapt("androidx.room:room-compiler:$roomVersion")

  // nav
  implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
  implementation("androidx.navigation:navigation-ui-ktx:$navVersion")

  // http
  implementation("com.squareup.okhttp3:okhttp:$okhttpVersion")

  // multidex
  implementation("androidx.multidex:multidex:2.0.1")

  // timber
  implementation("com.jakewharton.timber:timber:4.7.1")

  // insets
  implementation("dev.chrisbanes:insetter-ktx:0.3.1")

  // leakcanary
  debugImplementation("com.squareup.leakcanary:leakcanary-android:2.7")

  coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")

  // tests
  testImplementation("junit:junit:4.13.2")
  testImplementation("androidx.test:core:1.3.0")
  testImplementation("io.mockk:mockk:1.11.0")
  testImplementation("com.squareup.okhttp3:mockwebserver:$okhttpVersion")
  testImplementation("androidx.room:room-testing:$roomVersion")
  testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$kotlinCoroutinesVersion")
  androidTestImplementation(
    "org.jetbrains.kotlinx:kotlinx-coroutines-test:$kotlinCoroutinesVersion"
  )
  androidTestImplementation("androidx.arch.core:core-testing:2.1.0")
  androidTestImplementation("com.kaspersky.android-components:kaspresso:1.2.0")
  androidTestImplementation("app.cash.turbine:turbine:0.4.1")
  androidTestImplementation("androidx.test:runner:1.3.0")
  androidTestImplementation("androidx.test.ext:junit-ktx:1.1.3-alpha05")

  // dagger
  implementation("com.google.dagger:hilt-android:$hiltVersion")
  kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")
}
