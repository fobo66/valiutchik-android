import com.android.sdklib.AndroidVersion

plugins {
  id("com.android.application")
  kotlin("android")
  kotlin("kapt")
  id("dagger.hilt.android.plugin")
  id("io.gitlab.arturbosch.detekt")
  id("com.jaredsburrows.license")
}

val kotlinCoroutinesVersion = "1.5.2"
val hiltVersion = "2.41"
val roomVersion = "2.4.1"
val navVersion = "2.5.0-alpha02"
val lifecycleVersion = "2.4.1"
val flowBindingVersion = "1.2.0"
val retrofitVersion = "2.9.0"

android {
  signingConfigs {
    create("releaseSignConfig") {
      keyAlias = loadSecret(rootProject, KEY_ALIAS)
      keyPassword = loadSecret(rootProject, KEY_PASSWORD)
      storeFile = file(loadSecret(rootProject, STORE_FILE))
      storePassword = loadSecret(rootProject, STORE_PASSWORD)

      enableV3Signing = true
      enableV4Signing = true
    }
  }

  compileSdk = AndroidVersion.VersionCodes.S
  defaultConfig {
    applicationId = "fobo66.exchangecourcesbelarus"
    minSdk = AndroidVersion.VersionCodes.LOLLIPOP
    targetSdk = AndroidVersion.VersionCodes.S
    versionCode = 18
    versionName = "1.12.1"
    multiDexEnabled = true
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    resValue(
      "string",
      "mapboxGeocoderAccessToken",
      loadSecret(rootProject, MAPBOX_TOKEN)
    )

    resValue(
      "string",
      "apiUsername",
      loadSecret(rootProject, API_USERNAME)
    )

    resValue(
      "string",
      "apiPassword",
      loadSecret(rootProject, API_PASSWORD)
    )

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

    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }

  kotlinOptions {
    jvmTarget = "11"
  }

  kapt {
    arguments {
      arg("room.schemaLocation", "$projectDir/schemas")
      arg("room.incremental", "true")
      arg("room.expandProjection", "true")
    }
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

  testOptions {
    animationsDisabled = true
    unitTests.isIncludeAndroidResources = true
  }
}

detekt {
  autoCorrect = true
}

licenseReport {
  generateCsvReport = false
  generateHtmlReport = true
  generateJsonReport = false

  copyHtmlReportToAssets = true
  copyJsonReportToAssets = false
}

dependencies {
  implementation(project(":core"))

  // kotlin
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutinesVersion")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$kotlinCoroutinesVersion")

  // androidx
  implementation("androidx.annotation:annotation:1.3.0")
  implementation("androidx.activity:activity-ktx:1.4.0")
  implementation("androidx.recyclerview:recyclerview:1.2.1")
  implementation("androidx.fragment:fragment-ktx:1.4.1")
  implementation("androidx.collection:collection-ktx:1.2.0")
  implementation("androidx.core:core-ktx:1.7.0")
  implementation("androidx.constraintlayout:constraintlayout:2.1.3")
  implementation("com.google.android.material:material:1.5.0")
  implementation("androidx.preference:preference-ktx:1.2.0")
  implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.2.0-alpha01")

  // flowbinding

  implementation("io.github.reactivecircus.flowbinding:flowbinding-android:$flowBindingVersion")
  implementation("io.github.reactivecircus.flowbinding:flowbinding-swiperefreshlayout:$flowBindingVersion")
  implementation("io.github.reactivecircus.flowbinding:flowbinding-appcompat:$flowBindingVersion")
  implementation("io.github.reactivecircus.flowbinding:flowbinding-material:$flowBindingVersion")

  // lifecycle
  implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")

  // location
  implementation("com.mapbox.mapboxsdk:mapbox-sdk-services:5.9.0-alpha.1")

  // room
  implementation("androidx.room:room-runtime:$roomVersion")
  implementation("androidx.room:room-ktx:$roomVersion")
  kapt("androidx.room:room-compiler:$roomVersion")

  // nav
  implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
  implementation("androidx.navigation:navigation-ui-ktx:$navVersion")

  // http
  implementation(platform("com.squareup.okhttp3:okhttp-bom:5.0.0-alpha.4"))
  implementation("com.squareup.okhttp3:okhttp")
  debugImplementation("com.squareup.okhttp3:logging-interceptor")
  implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")

  // multidex
  implementation("androidx.multidex:multidex:2.0.1")

  // timber
  implementation("com.jakewharton.timber:timber:5.0.1")

  // insets
  implementation("dev.chrisbanes.insetter:insetter:0.6.1")

  // leakcanary
  debugImplementation("com.squareup.leakcanary:leakcanary-android:2.8.1")

  coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")

  detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.19.0")

  // tests
  testImplementation("junit:junit:4.13.2")
  testImplementation("androidx.test:core:1.4.0")
  testImplementation("io.mockk:mockk:1.12.2")
  testImplementation("com.squareup.retrofit2:retrofit-mock:$retrofitVersion")
  testImplementation("androidx.room:room-testing:$roomVersion")
  testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$kotlinCoroutinesVersion")
  androidTestImplementation(
    "org.jetbrains.kotlinx:kotlinx-coroutines-test:$kotlinCoroutinesVersion"
  )
  androidTestImplementation("androidx.arch.core:core-testing:2.1.0")
  androidTestImplementation("com.kaspersky.android-components:kaspresso:1.4.0")
  androidTestImplementation("app.cash.turbine:turbine:0.7.0")
  androidTestImplementation("androidx.test:runner:1.4.0")
  androidTestImplementation("androidx.test:rules:1.4.0")
  androidTestImplementation("androidx.test.espresso:espresso-contrib:3.4.0")
  androidTestImplementation("androidx.test.espresso:espresso-intents:3.4.0")
  androidTestImplementation("org.hamcrest:hamcrest-core:2.2")
  androidTestImplementation("androidx.test.ext:junit-ktx:1.1.3")

  // dagger
  implementation("com.google.dagger:hilt-android:$hiltVersion")
  kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")
}
