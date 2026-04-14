plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.appmapas"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.example.appmapas"
        minSdk = 24
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    // Mapa OpenStreetMap
    implementation("org.osmdroid:osmdroid-android:6.1.18")
    // Retrofit para llamadas HTTP a Nominatim
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // Glide (opcional, para cargar imágenes)
    implementation("com.github.bumptech.glide:glide:4.16.0")
    //Para usar el interceptor que ocupa nominatim para saber quien hace las peticiones
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    //Para cartas
    implementation("androidx.cardview:cardview:1.0.0")
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}