
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs")
}

android {
    namespace = "ru.elnorte.tfs_spring_2024_reshetnikov"
    compileSdk = 34

    defaultConfig {
        applicationId = "ru.elnorte.tfs_spring_2024_reshetnikov"
        minSdk = 26
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
    buildFeatures {
        viewBinding = true
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
    val kotlin = "1.12.0"
    val appcompat = "1.6.1"
    val material = "1.11.0"
    val constraintlayout = "2.1.4"
    val navigation = "2.7.7"
    val viewpager = "1.0.0"
    val livedata = "2.7.0"
    val lifecycle = "2.2.0"

    implementation("androidx.core:core-ktx:$kotlin")
    implementation("androidx.appcompat:appcompat:$appcompat")
    implementation("com.google.android.material:material:$material")
    implementation("androidx.constraintlayout:constraintlayout:$constraintlayout")

    implementation("androidx.navigation:navigation-fragment-ktx:$navigation")
    implementation("androidx.navigation:navigation-ui-ktx:$navigation")

    implementation("androidx.viewpager2:viewpager2:$viewpager")

    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$livedata")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$livedata")
    implementation("androidx.lifecycle:lifecycle-extensions:$lifecycle")
}
