import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.gradle.api.JavaVersion
import org.gradle.kotlin.dsl.dependencies

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.deliverybook"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.deliverybook"
        minSdk = 24
        targetSdk = 35
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
        debug {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Core Android
    add("implementation", "androidx.core:core-ktx:1.15.0")
    add("implementation", "androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    add("implementation", "androidx.activity:activity-compose:1.10.0")

    // Compose BOM and core UI
    add("implementation", platform("androidx.compose:compose-bom:2025.12.00"))
    add("implementation", "androidx.compose.ui:ui")
    add("implementation", "androidx.compose.ui:ui-graphics")
    add("implementation", "androidx.compose.material3:material3")
    add("implementation", "androidx.compose.ui:ui-tooling-preview")
    add("androidTestImplementation", platform("androidx.compose:compose-bom:2025.12.00"))
    add("androidTestImplementation", "androidx.compose.ui:ui-test-junit4")
    add("debugImplementation", "androidx.compose.ui:ui-tooling")
    add("debugImplementation", "androidx.compose.ui:ui-test-manifest")

    // Navigation
    add("implementation", "androidx.navigation:navigation-compose:2.8.8")
    add("implementation", "androidx.hilt:hilt-navigation-compose:1.2.0")

    // Hilt
    add("implementation", "com.google.dagger:hilt-android:2.51.1")
    add("ksp", "com.google.dagger:hilt-compiler:2.51.1")

    // Room
    add("implementation", "androidx.room:room-runtime:2.6.1")
    add("implementation", "androidx.room:room-ktx:2.6.1")
    add("ksp", "androidx.room:room-compiler:2.6.1")

    // Paging
    add("implementation", "androidx.paging:paging-runtime-ktx:3.3.2")
    add("implementation", "androidx.paging:paging-compose:3.3.2")

    // SplashScreen API
    add("implementation", "androidx.core:core-splashscreen:1.1.0")

    // Testing
    add("testImplementation", "junit:junit:4.13.2")
    add("testImplementation", "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    add("testImplementation", "com.google.truth:truth:1.4.4")
    add("testImplementation", "org.robolectric:robolectric:4.14.1")

    add("androidTestImplementation", "androidx.test.ext:junit:1.2.1")
    add("androidTestImplementation", "androidx.test.espresso:espresso-core:3.6.1")
}

