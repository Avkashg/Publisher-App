plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.publisherapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.publisherapp"
        minSdk = 24
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

    packagingOptions { resources.excludes.add("META-INF/*") }
}

dependencies {

    implementation("com.hivemq:hivemq-mqtt-client:1.3.3")
    implementation ("org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.5")
    implementation ("org.eclipse.paho:org.eclipse.paho.android.service:1.1.1")
    //implementation ("androidx.core:core-ktx:1.7.0")
    implementation ("com.google.android.gms:play-services-location:21.0.1")
    implementation ("androidx.localbroadcastmanager:localbroadcastmanager:1.0.0")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}