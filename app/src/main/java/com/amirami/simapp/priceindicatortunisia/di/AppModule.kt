package com.amirami.simapp.priceindicatortunisia.di

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseFirestore() = Firebase.firestore// FirebaseFirestore.getInstance()



    @Provides
    @Singleton
    fun provideRoomDatabaseCallback() : RoomDatabase.Callback {
        return object : RoomDatabase.Callback() {
        }
    }
}