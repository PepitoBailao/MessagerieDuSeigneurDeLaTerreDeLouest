plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "app.java.messagerieduseigneurdelaterredelouest"
    compileSdk = 34

    defaultConfig {
        applicationId = "app.java.messagerieduseigneurdelaterredelouest"
        minSdk = 31
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}

dependencies {
    // Firebase Authentication
    implementation("com.google.firebase:firebase-auth:21.0.1")
    // Bibliothèque pour ajuster la taille des éléments en fonction de la densité d'écran
    implementation("com.intuit.sdp:sdp-android:1.1.1")

    implementation(platform("com.google.firebase:firebase-bom:33.4.0"))
    implementation("com.google.firebase:firebase-analytics")

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
