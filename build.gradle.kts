// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }

    dependencies {
        classpath("com.google.gms:google-services:4.3.15") // Google Services plugin

        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.5")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files


        // Add the dependency for the Performance Monitoring plugin
        classpath("com.google.firebase:perf-plugin:1.4.2")  // Performance Monitoring plugin

//Dager hilt
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.46")

        classpath("org.jacoco:org.jacoco.core:0.8.8")



        classpath ("com.android.tools.build:gradle:8.0.1")
      //  classpath ("org.jetbrains.kotlin:kotlin-serialization:1.8.21")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.21")

    }
    allprojects {
        repositories {
            google()
            mavenCentral()

            maven { url = uri("https://jitpack.io") }


        }
    }

}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.google.devtools.ksp") version "1.8.21-1.0.11" apply false

}


tasks.register("clean", Delete::class) {
    delete (rootProject.buildDir)
}