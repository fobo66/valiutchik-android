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

val composeVersion = "1.3.2"
val composeUiVersion = "1.3.0-rc01"
val accompanistVersion = "0.26.5-beta"
val kotlinCoroutinesVersion = "1.6.4"
val hiltVersion = "2.44"
val activityVersion = "1.6.0"
val roomVersion = "2.4.3"
val navVersion = "2.5.2"
val lifecycleVersion = "2.6.0-alpha02"
val retrofitVersion = "2.9.0"
val mockkVersion = "1.13.2"
val junitVersion = "5.9.1"
val turbineVersion = "0.11.0"
val kaspressoVersion = "1.4.2"

android {
  signingConfigs {
    register("releaseSignConfig") {
      keyAlias = loadSecret(rootProject, KEY_ALIAS)
      keyPassword = loadSecret(rootProject, KEY_PASSWORD)
      storeFile = file(loadSecret(rootProject, STORE_FILE))
      storePassword = loadSecret(rootProject, STORE_PASSWORD)

      enableV3Signing = true
      enableV4Signing = true
    }
  }

  compileSdk = 33
  defaultConfig {
    applicationId = "fobo66.exchangecourcesbelarus"
    minSdk = AndroidVersion.VersionCodes.LOLLIPOP
    targetSdk = 33
    versionCode = 18
    versionName = "1.12.1"
    multiDexEnabled = true
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

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
    register("benchmark") {
      applicationIdSuffix = ".benchmark"
      signingConfig = signingConfigs.getByName("debug")
      matchingFallbacks += listOf("release")
      isDebuggable = false
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

    freeCompilerArgs = freeCompilerArgs + "-opt-in=kotlin.RequiresOptIn"
  }

  kapt {
    arguments {
      arg("room.schemaLocation", "$projectDir/schemas")
      arg("room.incremental", "true")
      arg("room.expandProjection", "true")
    }
  }

  buildFeatures {
    buildConfig = false
    compose = true
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

  composeOptions {
    kotlinCompilerExtensionVersion = composeVersion
  }
  namespace = "fobo66.exchangecourcesbelarus"
}

detekt {
  autoCorrect = true
}

licenseReport {
  generateCsvReport = false
  generateHtmlReport = false

  copyHtmlReportToAssets = false
  copyJsonReportToAssets = true
}

dependencies {
  api(project(":core"))
  api(project(":domain"))

  // kotlin
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutinesVersion")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$kotlinCoroutinesVersion")
  implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.5")

  // androidx
  implementation("androidx.annotation:annotation:1.5.0")
  implementation("androidx.activity:activity-compose:$activityVersion")
  implementation("androidx.core:core-ktx:1.9.0")
  implementation("com.google.android.material:material:1.8.0-alpha01")
  implementation("androidx.preference:preference-ktx:1.2.0")
  implementation("androidx.datastore:datastore-preferences:1.0.0")
  implementation("androidx.core:core-splashscreen:1.0.0")

  // compose
  implementation("androidx.compose.ui:ui:$composeUiVersion")
  implementation("androidx.compose.material3:material3:1.0.0-rc01")
  implementation("androidx.compose.ui:ui-tooling-preview:$composeUiVersion")
  implementation("androidx.activity:activity-compose:$activityVersion")
  androidTestImplementation("androidx.compose.ui:ui-test-junit4:$composeUiVersion")
  debugImplementation("androidx.compose.ui:ui-tooling:$composeUiVersion")

  implementation("com.google.accompanist:accompanist-swiperefresh:$accompanistVersion")
  implementation("com.google.accompanist:accompanist-permissions:$accompanistVersion")
  implementation("com.google.accompanist:accompanist-systemuicontroller:$accompanistVersion")

  // lifecycle
  implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")
  implementation("androidx.lifecycle:lifecycle-runtime-compose:$lifecycleVersion")
  implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")

  // room
  implementation("androidx.room:room-runtime:$roomVersion")
  implementation("androidx.room:room-ktx:$roomVersion")
  kapt("androidx.room:room-compiler:$roomVersion")

  // nav
  implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
  implementation("androidx.navigation:navigation-ui-ktx:$navVersion")
  implementation("androidx.navigation:navigation-compose:$navVersion")
  implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

  // multidex
  implementation("androidx.multidex:multidex:2.0.1")

  // timber
  implementation("com.jakewharton.timber:timber:5.0.1")

  // leakcanary
  debugImplementation("com.squareup.leakcanary:leakcanary-android:2.9.1")

  coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.0")

  detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.21.0")
  detektPlugins("com.twitter.compose.rules:detekt:0.0.18")

  // tests
  testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
  testImplementation("io.mockk:mockk:$mockkVersion")
  testImplementation("io.mockk:mockk-agent-jvm:$mockkVersion")
  testImplementation("com.squareup.retrofit2:retrofit-mock:$retrofitVersion")
  testImplementation("androidx.room:room-testing:$roomVersion")
  testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$kotlinCoroutinesVersion")
  testImplementation("app.cash.turbine:turbine:$turbineVersion")
  testImplementation("com.google.truth:truth:1.1.3")
  androidTestImplementation("androidx.test:core:1.5.0-beta01")
  androidTestImplementation(
    "org.jetbrains.kotlinx:kotlinx-coroutines-test:$kotlinCoroutinesVersion"
  )
  androidTestImplementation("androidx.arch.core:core-testing:2.1.0")
  androidTestImplementation("com.kaspersky.android-components:kaspresso:$kaspressoVersion")
  androidTestImplementation(
    "com.kaspersky.android-components:kaspresso-compose-support:$kaspressoVersion"
  )
  androidTestImplementation("io.github.kakaocup:compose:0.1.0")
  androidTestImplementation("app.cash.turbine:turbine:$turbineVersion")
  androidTestImplementation("androidx.test:runner:1.5.0-beta01")
  androidTestImplementation("androidx.test:rules:1.4.1-beta01")
  androidTestImplementation("androidx.test.espresso:espresso-contrib:3.5.0-beta01")
  androidTestImplementation("androidx.test.espresso:espresso-intents:3.5.0-beta01")
  androidTestImplementation("org.hamcrest:hamcrest-core:2.2")
  androidTestImplementation("androidx.test.ext:junit-ktx:1.1.4-beta01")
  androidTestImplementation("androidx.compose.ui:ui-test-junit4:$composeUiVersion")
  debugImplementation("androidx.compose.ui:ui-test-manifest:$composeUiVersion")

  // dagger
  implementation("com.google.dagger:hilt-android:$hiltVersion")
  kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")
}
