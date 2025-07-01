import com.google.firebase.appdistribution.gradle.firebaseAppDistribution
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.firebase.app.distribution)
    alias(libs.plugins.kotlin.serialization)
}

// Helper function to read local.properties
fun getLocalProperty(key: String): String {
    val localProperties = Properties().apply {
        load(rootProject.file("local.properties").inputStream())
    }
    return localProperties.getProperty(key) ?: error("Property $key not found in local.properties")
}


android {
    namespace = "com.paandaaa.homey.android"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.paandaaa.homey.android"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.5"
        // Add these to defaultConfig so they're available in all build types
        buildConfigField("String", "SUPABASE_URL", "\"${getLocalProperty("SUPABASE_URL")}\"")
        buildConfigField("String", "SUPABASE_KEY", "\"${getLocalProperty("SUPABASE_KEY")}\"")
        buildConfigField("String", "GOOGLE_WEB_CLIENT_ID", "\"${getLocalProperty("GOOGLE_WEB_CLIENT_ID")}\"")
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            firebaseAppDistribution {
                appId = "1:541993050819:android:2e91ab1e6419f05114b876"
                artifactType = "APK"
                groups = "FYP"
                releaseNotes = "Release notes for the latest version"
            }
        }
        getByName("release") {
            isMinifyEnabled = false
            firebaseAppDistribution {
                appId = "1:541993050819:android:2e91ab1e6419f05114b876"
                artifactType = "APK"
                groups = "FYP"
                releaseNotes = "Release notes for the latest version"
            }
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
    implementation(projects.shared)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.play.services.auth)
    implementation(libs.google.generativeai)
    debugImplementation(libs.compose.ui.tooling)
    implementation(libs.navigation.compose)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    implementation(libs.compose.ui.google.fonts)

    implementation(platform(libs.supabase.bom))
    implementation(libs.supabase.postgrest)
    implementation(libs.supabase.auth)
    implementation(libs.supabase.realtime)

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.timeout)

    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services)
    implementation(libs.googleid)

    implementation(libs.coil.compose)
    implementation(libs.coil.gif)

    implementation(libs.material.icons.extended)

    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.kotlinx.serialization)
    implementation(libs.converter.gson)
    implementation(libs.play.services.location)

    implementation(libs.google.maps.compose)
    implementation(libs.play.services.map)

    implementation(libs.datastore.preferences)
    implementation(libs.datastore.preferences.rxjava2)
    implementation(libs.datastore.preferences.rxjava3)
    implementation(libs.datastore.preferences.core)

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.coroutines.core)

    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)

    implementation(libs.gson)

    implementation(libs.google.generativeai)
}