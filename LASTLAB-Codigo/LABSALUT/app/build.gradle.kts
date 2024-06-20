import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    id("com.google.secrets_gradle_plugin") version "0.5"
}

android {
    namespace = "es.ifp.labsalut"
    compileSdk = 34

    defaultConfig {
        applicationId = "es.ifp.labsalut"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Cargar los secretos desde secrets.properties
        val secretsPropertiesFile = rootProject.file("secrets.properties")
        if (secretsPropertiesFile.exists()) {
            val secretsProperties = Properties().apply {
                load(secretsPropertiesFile.inputStream())
            }
            // Convertir a String explícitamente para evitar problemas de tipos
            val apiKey = secretsProperties.getProperty("MAPS_API_KEY") ?: ""
            manifestPlaceholders["MAPS_API_KEY"] = apiKey
            // Agregar el valor de la clave API como recurso string
            resValue("string", "google_maps_key", apiKey)  // Aquí se usa directamente apiKey
        } else {
            val apiKey = System.getenv("MAPS_API_KEY") ?: ""
            manifestPlaceholders["MAPS_API_KEY"] = apiKey
            // Agregar el valor de la clave API como recurso string
            resValue("string", "google_maps_key", apiKey)  // Aquí se usa directamente apiKey
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.preference)
    implementation(libs.biometric)
    implementation(libs.security.crypto)
    implementation(libs.material.v1120)
    implementation(libs.places)
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}