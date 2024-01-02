plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
//    id("com.google.dagger.hilt.android")
//    id("com.google.devtools.ksp")
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
}

dependencies {
//    api(project(":craft-essentials"))
//    application(project(":things"))
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    api("androidx.appcompat:appcompat:1.6.1")
    api("androidx.recyclerview:recyclerview:1.3.2")
    api("androidx.palette:palette-ktx:1.0.0")
    api("androidx.preference:preference-ktx:1.2.1")
    api("androidx.legacy:legacy-preference-v14:1.0.0")
    api("com.google.android.material:material:1.11.0")

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
}
