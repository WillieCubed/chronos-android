plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.craft.apps.countdowns"
    compileSdk = 34

    defaultConfig {
        minSdk = 29

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    buildFeatures {
        buildConfig = true
    }
    buildTypes {
        release {
            buildConfigField("Boolean", "DEBUG", "false")
            isMinifyEnabled = false
//            proguardFiles getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro"
        }
        debug {
            buildConfigField("Boolean", "DEBUG", "true")
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

    api("androidx.appcompat:appcompat:1.6.1")
    api("androidx.recyclerview:recyclerview:1.3.2")
    api("androidx.palette:palette-ktx:1.0.0")
    api("androidx.preference:preference-ktx:1.2.1")
    api("androidx.legacy:legacy-preference-v14:1.0.0")
    api("com.google.android.material:material:1.11.0")

    // --- Old stuff ---

    api(platform("com.google.firebase:firebase-bom:32.7.0"))
    api("com.google.firebase:firebase-database")
    api("com.google.firebase:firebase-firestore")
    api("com.google.firebase:firebase-messaging")
    api("com.firebaseui:firebase-ui-database:8.0.2")
    implementation("com.google.android.gms:play-services-measurement-api:21.5.0")
    implementation("joda-time:joda-time:2.12.5")
    api("de.hdodenhof:circleimageview:3.1.0")
    api("com.github.bumptech.glide:glide:4.16.0")
    testImplementation("junit:junit:4.13.2")

    // ------

    // Room
    val roomVersion = "2.6.1"

    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")

    // Jetpack Compose
    val composeBom = platform("androidx.compose:compose-bom:2023.08.00")

    implementation(composeBom)
    api("androidx.compose.material:material")
    api("androidx.compose.material3:material3")
    api("androidx.compose.material3:material3-window-size-class")
    api("androidx.compose.material:material-icons-extended")
    api("androidx.compose.ui:ui")
    api("androidx.compose.ui:ui-graphics")
    api("androidx.compose.ui:ui-tooling-preview")
    api("androidx.activity:activity-compose:1.8.2")
    api("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")

    // Hilt
    api("com.google.dagger:hilt-android:2.50")
    api("androidx.hilt:hilt-navigation-compose:1.1.0")
    api("androidx.hilt:hilt-work:1.1.0")
    ksp("androidx.hilt:hilt-compiler:1.1.0")
    ksp("com.google.dagger:hilt-android-compiler:2.50")

    // Android Work
    val workVersion = "2.9.0"
    api("androidx.work:work-runtime-ktx:$workVersion")

    // Other stuff
    api("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    api("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation(composeBom)
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.work:work-testing:$workVersion")
}
