plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    // Aggiungi il plugin di Google Services
    alias(libs.plugins.google.services)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.example.gardenbuddy"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.gardenbuddy"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        android.buildFeatures.buildConfig = true
        val apiKeyPlant: String = project.findProperty("apiKeyPlant") as String? ?: ""
        buildConfigField("String", "API_KEY_PLANT", "\"$apiKeyPlant\"")
        android.buildFeatures.buildConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}
//1

dependencies {

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.firebase.common.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.runtime.livedata)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.kotlinx.serialization.json)
    //implementation(libs.okhttp) // OkHttp
    implementation(libs.coil.compose)
    implementation(libs.accompanist.pager)
    implementation(libs.accompanist.pager.indicators)
    // Firebase BoM to manage consistent versions
    implementation(platform(libs.firebase.bom))
    implementation(libs.material.icons.extended)

    // Add Firebase dependencies (only the necessary ones)
    implementation(libs.firebase.firestore) // Firestore Database
    implementation(libs.firebase.auth) // Firebase Authentication
    implementation(libs.firebase.database) // Realtime Database

    // Retrofit library
    implementation(libs.retrofit)

    // Gson Converter (to parse JSON)
    implementation(libs.retrofit.converter.gson)


}