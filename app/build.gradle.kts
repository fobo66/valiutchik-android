import com.android.sdklib.AndroidVersion

plugins {
  id("com.android.application")
  kotlin("android")
  kotlin("kapt")
  id("dagger.hilt.android.plugin")
  id("io.gitlab.arturbosch.detekt")
  id("com.jaredsburrows.license")
  id("de.mannodermaus.android-junit5")
}

val kotlinCoroutinesVersion = "1.6.3"
val hiltVersion = "2.42"
val roomVersion = "2.4.2"
val navVersion = "2.4.2"
val lifecycleVersion = "2.4.1"
val flowBindingVersion = "1.2.0"
val retrofitVersion = "2.9.0"
val mockkVersion = "1.12.4"
val junitVersion = "5.8.2"

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
  namespace = "fobo66.exchangecourcesbelarus"
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
  implementation("androidx.annotation:annotation:1.4.0")
  implementation("androidx.activity:activity-ktx:1.4.0")
  implementation("androidx.recyclerview:recyclerview:1.2.1")
  implementation("androidx.fragment:fragment-ktx:1.5.0-rc01")
  implementation("androidx.collection:collection-ktx:1.2.0")
  implementation("androidx.core:core-ktx:1.8.0-beta01")
  implementation("androidx.constraintlayout:constraintlayout:2.1.4")
  implementation("com.google.android.material:material:1.7.0-alpha02")
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
  implementation("com.mapbox.search:mapbox-search-android:1.0.0-beta.32")

  // room
  implementation("androidx.room:room-runtime:$roomVersion")
  implementation("androidx.room:room-ktx:$roomVersion")
  kapt("androidx.room:room-compiler:$roomVersion")

  // nav
  implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
  implementation("androidx.navigation:navigation-ui-ktx:$navVersion")

  // http
  implementation(platform("com.squareup.okhttp3:okhttp-bom:5.0.0-alpha.9"))
  implementation("com.squareup.okhttp3:okhttp")
  debugImplementation("com.squareup.okhttp3:logging-interceptor")
  implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")

  // multidex
  implementation("androidx.multidex:multidex:2.0.1")

  // timber
  implementation("com.jakewharton.timber:timber:5.0.1")

  // insets
  implementation("dev.chrisbanes.insetter:insetter:0.6.1")

  coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.6")

  detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.20.0")

  // tests
  testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
  testImplementation("androidx.test:core:1.5.0-alpha01")
  testImplementation("io.mockk:mockk:$mockkVersion")
  testImplementation("io.mockk:mockk-agent-jvm:$mockkVersion")
  testImplementation("com.squareup.retrofit2:retrofit-mock:$retrofitVersion")
  testImplementation("androidx.room:room-testing:$roomVersion")
  testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$kotlinCoroutinesVersion")
  androidTestImplementation(
    "org.jetbrains.kotlinx:kotlinx-coroutines-test:$kotlinCoroutinesVersion"
  )
  androidTestImplementation("androidx.arch.core:core-testing:2.1.0")
  androidTestImplementation("com.kaspersky.android-components:kaspresso:1.4.1")
  androidTestImplementation("app.cash.turbine:turbine:0.8.0")
  androidTestImplementation("androidx.test:runner:1.5.0-alpha04")
  androidTestImplementation("androidx.test:rules:1.4.1-alpha07")
  androidTestImplementation("androidx.test.espresso:espresso-contrib:3.5.0-alpha07")
  androidTestImplementation("androidx.test.espresso:espresso-intents:3.5.0-alpha07")
  androidTestImplementation("org.hamcrest:hamcrest-core:2.2")
  androidTestImplementation("androidx.test.ext:junit-ktx:1.1.4-alpha07")

  // dagger
  implementation("com.google.dagger:hilt-android:$hiltVersion")
  kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")
}
