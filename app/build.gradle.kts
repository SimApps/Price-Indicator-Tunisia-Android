plugins {
    id("com.android.application")
    id("kotlin-android")
    id("com.google.gms.google-services")

// Apply the Crashlytics Gradle plugin
    id("com.google.firebase.crashlytics")

    // Apply the Performance Monitoring plugin
    id("com.google.firebase.firebase-perf")

    // dagger hilt
    id("dagger.hilt.android.plugin")

    id("kotlin-kapt")

    id("com.google.devtools.ksp")
}


android {

    compileSdk = 33
    defaultConfig {
        applicationId = "com.amirami.simapp.priceindicatortunisia"
        minSdk = 21
        targetSdk = 33
        versionCode = 19
        versionName = "@string/app_version"
        multiDexEnabled = true
        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {



        release {

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
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
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.7"
    }


    /*packagingOptions {
        resources {
            excludes += '/META-INF/{AL2.0,LGPL2.1}'
        }
    }*/
    namespace = "com.amirami.simapp.priceindicatortunisia"
}

dependencies {
    implementation("androidx.multidex:multidex:2.0.1")

    implementation("androidx.core:core-ktx:1.10.0")
    implementation("androidx.appcompat:appcompat:1.6.1")

    implementation(platform("androidx.compose:compose-bom:2023.05.00"))
    implementation("androidx.compose.ui:ui")
    // implementation ("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    // implementation ("androidx.compose.material3:material3")
    // Override Material Design 3 library version with a pre-release version
    implementation("androidx.compose.material3:material3:1.1.0-rc01")
    implementation("androidx.compose.ui:ui-util")
    implementation("androidx.compose.material:material-icons-extended:1.5.0-alpha03")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    implementation("com.google.accompanist:accompanist-systemuicontroller:0.30.0")

    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("androidx.navigation:navigation-compose:2.5.3")


    implementation("androidx.paging:paging-runtime-ktx:3.1.1")

    implementation("androidx.core:core-splashscreen:1.0.1")



    // Coil
    implementation("io.coil-kt:coil-compose:2.3.0")




// admob
    implementation("com.google.android.gms:play-services-ads:22.0.0")
    // custom toast
    implementation("com.pranavpandey.android:dynamic-toasts:4.1.3")

    // firebase auth
    implementation("com.firebaseui:firebase-ui-auth:8.0.2")

    // Import the BoM for the Firebase platform
    implementation(platform("com.google.firebase:firebase-bom:30.3.1"))

    // firebase firestore
    implementation("com.firebaseui:firebase-ui-firestore:8.0.2")
    implementation("com.google.firebase:firebase-firestore-ktx")

    implementation("com.google.firebase:firebase-perf-ktx")

    // Declare the dependencies for the Crashlytics and Analytics libraries
    // When using the BoM, you don")t specify versions in Firebase library dependencies
    // implementation ")com.google.firebase:firebase-crashlytics")
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    //  implementation ("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-analytics-ktx")

    // real time database
    implementation("com.google.firebase:firebase-database-ktx")

    implementation ("com.google.firebase:firebase-auth-ktx")

    // check internet connection
    implementation("com.github.SalahoAmro:NetDetect:1.0.0")

    // graph
    implementation("com.github.blackfizz:eazegraph:1.2.2@aar")
    implementation("com.nineoldandroids:library:2.4.0")

// GSON
    implementation("com.google.code.gson:gson:2.10.1")


  //  implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
    implementation ("androidx.lifecycle:lifecycle-runtime-compose:2.6.1")


    // Play Services
    implementation("com.google.android.gms:play-services-auth:20.5.0")

    // Firebase
    // implementation platform("com.google.firebase:firebase-bom:28.4.0")
    //  implementation "com.google.firebase:firebase-analytics-ktx"
    //  implementation "com.google.firebase:firebase-auth-ktx"
    //  implementation "com.google.firebase:firebase-firestore-ktx"
    //  implementation "com.google.firebase:firebase-database-ktx"



    implementation("com.google.devtools.ksp:symbol-processing-api:1.8.21-1.0.11")




// Data store ;;;; like shared pref

    implementation("androidx.datastore:datastore-preferences:1.0.0")


    // kotlin caroutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.1")

    // For instrumentation tests
    // androidTestImplementation  ")com.google.dagger:hilt-android-testing:2.40.1")
    //  kaptAndroidTest ")com.google.dagger:hilt-compiler:2.40.1")


    // in app review
    //  implementation ")com.google.android.play:core:1.10.3")
    implementation("com.google.android.play:core-ktx:1.8.1")


    // - - Room Persistence Library
    val room_version = "2.5.1"

    implementation("androidx.room:room-runtime:$room_version")
    ksp("androidx.room:room-compiler:$room_version")
    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$room_version")





    /** Dagger hilt */
    implementation("com.google.dagger:hilt-android:2.46.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    testImplementation("junit:junit:4.13.2")
    kapt("com.google.dagger:hilt-compiler:2.46.1")
    kapt("androidx.hilt:hilt-compiler:1.0.0")

    implementation("com.airbnb.android:lottie-compose:6.0.0")


    // Accompanist
    val accompanist_version = "0.30.0"

    implementation("com.google.accompanist:accompanist-insets:$accompanist_version")
    //noinspection GradleDependency
    implementation("com.google.accompanist:accompanist-systemuicontroller:$accompanist_version")
    implementation("com.google.accompanist:accompanist-pager:$accompanist_version")
    implementation("com.google.accompanist:accompanist-drawablepainter:$accompanist_version")

    // If using indicators, also depend on
    implementation("com.google.accompanist:accompanist-pager-indicators:$accompanist_version")


    //  Camerax dependencies
    implementation("androidx.camera:camera-core:1.2.2")
    implementation("androidx.camera:camera-camera2:1.2.2")
    implementation("androidx.camera:camera-lifecycle:1.2.2")
    implementation("androidx.camera:camera-view:1.3.0-alpha06")
    implementation("androidx.camera:camera-extensions:1.3.0-alpha06")




    // permission handling in compose
    implementation("com.google.accompanist:accompanist-permissions:0.30.0")


    // MLKit
    implementation("com.google.android.gms:play-services-mlkit-text-recognition:19.0.0")
// Barcode model
    implementation("com.google.mlkit:barcode-scanning:17.1.0")
// TO GENERATE BARCODE MAYBE SEE IF  I CAN USE : com.google.mlkit:barcode-scanning
    implementation("com.google.zxing:core:3.5.1")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.8.21")
}
repositories {
    mavenCentral()
}

kapt {
    correctErrorTypes = true
}
