plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

android {
    namespace = "com.example.timevision_application"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.timevision_application"
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:32.8.1"))
    //Circluar Image Library
    implementation("de.hdodenhof:circleimageview:3.1.0")

    implementation("com.google.firebase:firebase-analytics")
    //Firebase Libraries
    implementation("com.google.firebase:firebase-auth:22.3.1")
    implementation("com.google.firebase:firebase-database:20.3.1")
    implementation("com.google.firebase:firebase-storage-ktx:19.2.2")
    //Progress Dialog
    implementation("com.jpardogo.googleprogressbar:library:1.2.0")

    // adding a implementation of card view
    implementation("androidx.cardview:cardview:1.0.0")

    implementation("com.github.bumptech.glide:glide:4.12.0")

    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")

    //Adding the implementation for the date range picker
    implementation("com.google.android.material:material:1.8.0")
    //Adding the link here for github
    implementation("com.github.AnyChart:AnyChart-Android:1.1.5")
}