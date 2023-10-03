// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
    mavenCentral()
}
    dependencies {
        classpath ("com.android.tools.build:gradle:8.3.0-alpha06")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.21")
        //noinspection GradleDependency
        classpath ("com.google.gms:google-services:4.3.10")
// Add the Crashlytics Gradle plugin (be sure to add version
        // 2.0.0 or later if you built your app with Android Studio 4.1).
        classpath ("com.google.firebase:firebase-crashlytics-gradle:2.9.9")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files


        // Add the dependency for the Performance Monitoring plugin
        classpath ("com.google.firebase:perf-plugin:1.4.2")  // Performance Monitoring plugin

//Dager hilt
        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.48")



        classpath ("androidx.navigation:navigation-safe-args-gradle-plugin:2.7.3")

    }


}
plugins {
    id("com.google.devtools.ksp") version "1.9.10-1.0.13" apply false
}


allprojects {
    repositories {
        google()
        mavenCentral()

        maven { url = uri("https://jitpack.io") }
    }
}

tasks.register("clean", Delete::class) {
    delete (rootProject.buildDir)
}
