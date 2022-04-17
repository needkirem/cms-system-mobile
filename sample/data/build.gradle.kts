plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    kotlin("plugin.serialization") version "1.6.10"
}

android {
    compileSdk = 32

    defaultConfig {
        minSdk = 21
        targetSdk = 32

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    val jsonBaseUrl = "https://raw.githubusercontent.com/"
    val jsonPath = "needkirem/cms-system-mobile/main/sample/sample.json"

    buildTypes {
        debug {
            buildConfigField("String", "jsonBaseUrl", "\"$jsonBaseUrl\"")
            buildConfigField("String", "jsonPath", "\"$jsonPath\"")
        }
        release {
            buildConfigField("String", "jsonBaseUrl", "\"$jsonBaseUrl\"")
            buildConfigField("String", "jsonPath", "\"$jsonPath\"")
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")
    implementation(project(":sample:domain"))
    implementation(project(":cms-system"))
    kapt(project(":cms-system"))
}