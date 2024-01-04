plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    kotlin("kapt")
}

android {
    namespace = "com.craft.apps.countdowns"
    compileSdk = 34

    defaultConfig {
        minSdk = 29
        targetSdk = 34
        versionCode = 3
        versionName = "v2.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }
    buildTypes {
        debug {
            isMinifyEnabled = false
        }
        release {
            isMinifyEnabled = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.7"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

hilt {
    enableAggregatingTask = true
}

dependencies {
    // Module with base functionality
    implementation(project(":base"))

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")

    // AndroidX Core
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")

    implementation("androidx.palette:palette-ktx:1.0.0")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.browser:browser:1.7.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.core:core-splashscreen:1.0.1")

    // AppSearch
    val appsearch_version = "1.1.0-alpha03"

    implementation("androidx.appsearch:appsearch:$appsearch_version")
    implementation("androidx.appsearch:appsearch-local-storage:$appsearch_version")
    implementation("androidx.appsearch:appsearch-platform-storage:$appsearch_version")
    // AppSearch 1.1.0-alpha03 doesn't play well with KSP yet for some reason
    kapt("androidx.appsearch:appsearch-compiler:$appsearch_version")

    // Room

    val room_version = "2.6.1"

    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    ksp("androidx.room:room-compiler:$room_version")

    // Jetpack Compose
    val composeBom = platform("androidx.compose:compose-bom:2023.08.00")

    implementation(composeBom)
    implementation("androidx.compose.material:material")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material3:material3-window-size-class")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.50")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
    implementation("androidx.hilt:hilt-work:1.1.0")
    ksp("com.google.dagger:hilt-android-compiler:2.50")
    ksp("androidx.hilt:hilt-compiler:1.1.0")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-perf")
    implementation("com.firebaseui:firebase-ui-auth:8.0.2")
    implementation("com.firebaseui:firebase-ui-storage:8.0.2")

    // Other Google Mobile Services stuff
    implementation("com.google.android.gms:play-services-ads:22.6.0")
    implementation("com.google.android.gms:play-services-instantapps:18.0.1")
    implementation("com.android.billingclient:billing:6.1.0")

    // Others
    implementation("pub.devrel:easypermissions:3.0.0")
    implementation("com.getkeepsafe.taptargetview:taptargetview:1.13.3")
    implementation("com.github.paolorotolo:appintro:4.1.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation(composeBom)
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
