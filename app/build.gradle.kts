plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
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
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    //Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.31")
    implementation("androidx.core:core-ktx:1.0.1")

    //Appcompat
    implementation("androidx.appcompat:appcompat:1.0.2")

    //Constraintlayout
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")

    //Test
    testImplementation("junit:junit:4.12")
    androidTestImplementation("androidx.test:runner:1.1.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.1.1")

    //Kodein
    implementation("org.kodein.di:kodein-di-generic-jvm:6.2.0")
    implementation("org.kodein.di:kodein-di-framework-android-x:6.2.0")

    //Retrofit 2
    implementation ("com.squareup.retrofit2:retrofit:2.5.0")

    //Easy permissions
    implementation ("pub.devrel:easypermissions:2.0.0")

    //Room
    val roomVersion = "2.1.0-alpha06"
    implementation ("androidx.room:room-runtime:$roomVersion")
    kapt ("androidx.room:room-compiler:$roomVersion")
    kapt ("androidx.room:room-compiler:2.1.0-alpha07")

    //Anko
    val ankoVersion = "0.10.8"
    implementation ("org.jetbrains.anko:anko:$ankoVersion")
    implementation ("org.jetbrains.anko:anko-commons:$ankoVersion")

    //Coroutines
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.2.1")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.2.1")
}