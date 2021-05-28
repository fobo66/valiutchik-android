import io.gitlab.arturbosch.detekt.Detekt

plugins {
  id("com.android.application")
  kotlin("android")
  kotlin("kapt")
  id("dagger.hilt.android.plugin")
  id("io.gitlab.arturbosch.detekt").version("1.17.1")
  id("com.google.android.gms.oss-licenses-plugin")
}

val kotlinCoroutinesVersion = "1.5.0"
val hiltVersion = "2.36"
val roomVersion = "2.4.0-alpha02"
val navVersion = "2.4.0-alpha01"
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

  compileSdkPreview = "android-S"
  defaultConfig {
    applicationId = "fobo66.exchangecourcesbelarus"
    minSdk = 18
    targetSdkPreview = "S"
    versionCode = 17
    versionName = "1.12"
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
  }

  buildFeatures {
    viewBinding = true
    buildConfig = false
  }

  packagingOptions {
    resources {
      excludes += "META-INF/AL2.0"
      excludes += "META-INF/LGPL2.1"
    }
  }

  testOptions.unitTests.isIncludeAndroidResources = true
}

tasks.withType<Detekt>().configureEach {
  // Target version of the generated JVM bytecode. It is used for type resolution.
  jvmTarget = "1.8"
}

detekt {
  autoCorrect = true
}

dependencies {
  implementation(project(":core"))

  // kotlin
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutinesVersion")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$kotlinCoroutinesVersion")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:$kotlinCoroutinesVersion")

  // androidx
  implementation("androidx.annotation:annotation:1.3.0-alpha01")
  implementation("androidx.activity:activity-ktx:1.3.0-alpha08")
  implementation("androidx.recyclerview:recyclerview:1.2.0")
  implementation("androidx.fragment:fragment-ktx:1.4.0-alpha01")
  implementation("androidx.collection:collection-ktx:1.2.0-alpha01")
  implementation("androidx.core:core-ktx:1.6.0-beta01")
  implementation("androidx.constraintlayout:constraintlayout:2.1.0-beta02")
  implementation("com.google.android.material:material:1.4.0-beta01")
  implementation("androidx.preference:preference-ktx:1.1.1")
  implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.2.0-alpha01")

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
  implementation(platform("com.squareup.okhttp3:okhttp-bom:5.0.0-alpha.2"))
  implementation("com.squareup.okhttp3:okhttp")
  implementation("com.squareup.okhttp3:logging-interceptor")

  // multidex
  implementation("androidx.multidex:multidex:2.0.1")

  // timber
  implementation("com.jakewharton.timber:timber:4.7.1")

  // insets
  implementation("dev.chrisbanes.insetter:insetter:0.6.0")

  // leakcanary
  debugImplementation("com.squareup.leakcanary:leakcanary-android:2.7")

  implementation("com.google.android.gms:play-services-oss-licenses:17.0.0")

  coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")

  detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.17.1")

  // tests
  testImplementation("junit:junit:4.13.2")
  testImplementation("androidx.test:core:1.4.0-beta01")
  testImplementation("io.mockk:mockk:1.11.0")
  testImplementation("com.squareup.okhttp3:mockwebserver3")
  testImplementation("com.squareup.okhttp3:mockwebserver3-junit4")
  testImplementation("androidx.room:room-testing:$roomVersion")
  testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$kotlinCoroutinesVersion")
  androidTestImplementation(
    "org.jetbrains.kotlinx:kotlinx-coroutines-test:$kotlinCoroutinesVersion"
  )
  androidTestImplementation("androidx.arch.core:core-testing:2.1.0")
  androidTestImplementation("com.kaspersky.android-components:kaspresso:1.2.0")
  androidTestImplementation("app.cash.turbine:turbine:0.5.1")
  androidTestImplementation("androidx.test:runner:1.4.0-beta01")
  androidTestImplementation("androidx.test.espresso:espresso-contrib:3.4.0-beta01")
  androidTestImplementation("androidx.test.ext:junit-ktx:1.1.3-beta01")

  // dagger
  implementation("com.google.dagger:hilt-android:$hiltVersion")
  kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")
}
