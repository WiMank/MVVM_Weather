plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("kotlinx-serialization")
}

android {
    compileSdkVersion(28)
    defaultConfig {
        applicationId = "com.wimank.mvvm.weather"
        minSdkVersion(21)
        targetSdkVersion(28)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    dataBinding {
        isEnabled = true
    }

    packagingOptions {
        exclude("META-INF/ktor-http.kotlin_module")
        exclude("META-INF/kotlinx-io.kotlin_module")
        exclude("META-INF/atomicfu.kotlin_module")
        exclude("META-INF/ktor-utils.kotlin_module")
        exclude("META-INF/kotlinx-coroutines-io.kotlin_module")
        exclude("META-INF/kotlinx-coroutines-core.kotlin_module")
        exclude("META-INF/ktor-http-cio.kotlin_module")
        exclude("META-INF/ktor-client-core.kotlin_module")
    }
}


dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    //Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.31")
    implementation("androidx.core:core-ktx:1.0.2")

    //Appcompat
    implementation("androidx.appcompat:appcompat:1.0.2")

    //Constraintlayout
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")

    //Test
    testImplementation("junit:junit:4.12")
    androidTestImplementation("androidx.test:runner:1.1.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.1.1")

    //Kodein
    val kodeinVersion = "6.2.0"
    implementation("org.kodein.di:kodein-di-generic-jvm:$kodeinVersion")
    implementation("org.kodein.di:kodein-di-framework-android-x:$kodeinVersion")

    //Easy permissions
    implementation("pub.devrel:easypermissions:2.0.0")

    //Room
    val roomVersion = "2.1.0-rc01"
    implementation("androidx.room:room-runtime:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    implementation("androidx.room:room-rxjava2:$roomVersion")

    //Anko
    val ankoVersion = "0.10.8"
    implementation("org.jetbrains.anko:anko:$ankoVersion")
    implementation("org.jetbrains.anko:anko-commons:$ankoVersion")

    //Coroutines
    val coroutinesVersion = "1.2.1"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")

    //RX
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")
    implementation("io.reactivex.rxjava2:rxkotlin:2.3.0")

    //Lifecycle
    val lifecycleVersion = "2.2.0-alpha01"
    implementation("androidx.lifecycle:lifecycle-extensions:$lifecycleVersion")

    //Location
    implementation("com.google.android.gms:play-services-location:16.0.0")

    //Ktor
    val ktorVersion = "1.2.0"
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-android:$ktorVersion")
    implementation("io.ktor:ktor-client-json-jvm:$ktorVersion")
    implementation("io.ktor:ktor-client-gson:$ktorVersion")
    implementation("io.ktor:ktor-client-logging-jvm:$ktorVersion")

    //MapBox
    implementation("com.mapbox.mapboxsdk:mapbox-android-plugin-places-v7:0.8.0")

    val retrofitVersion = "2.5.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:3.12.0")
}