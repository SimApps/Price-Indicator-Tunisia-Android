package com.amirami.simapp.priceindicatortunisia.di

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import com.amirami.simapp.priceindicatortunisia.fidcard.FidCardDao
import com.amirami.simapp.priceindicatortunisia.fidcard.FidCardDatabase
import com.amirami.simapp.priceindicatortunisia.productnames.ProdNamesDAO
import com.amirami.simapp.priceindicatortunisia.productnames.ProdNamesDatabase
import com.amirami.simapp.priceindicatortunisia.shoping.ProdShopListDAO
import com.amirami.simapp.priceindicatortunisia.shoping.ProdShopListDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
     fun provideProdNamesDataBase(application : Application, roomCallback : RoomDatabase.Callback) : ProdNamesDatabase {
        return Room.databaseBuilder(
            application.applicationContext,
            ProdNamesDatabase::class.java,
            "productnames_database"
        )
            .fallbackToDestructiveMigration()
            .addCallback(roomCallback)
            .build()
    }


    @Provides
    @Singleton
    fun provideProdShopListDataBase(application : Application, roomCallback : RoomDatabase.Callback) : ProdShopListDatabase {
        return Room.databaseBuilder(
            application.applicationContext,
            ProdShopListDatabase::class.java,
            "productshoplist_database"
        )
            .fallbackToDestructiveMigration()
            .addCallback(roomCallback)
            .build()
    }


    @Provides
    @Singleton
    fun provideFidCardDataBase(application : Application, roomCallback : RoomDatabase.Callback) : FidCardDatabase {
        return Room.databaseBuilder(
            application.applicationContext,
            FidCardDatabase::class.java,
            "fidCardDatabase_database"
        )
            .fallbackToDestructiveMigration()
            .addCallback(roomCallback)
            .build()
    }

    @Provides
     fun provideRoomDatabaseCallback() : RoomDatabase.Callback {
        return object : RoomDatabase.Callback() {
        }
    }

    @Provides
     fun provideProdNamesDAO(prodNamesDatabase : ProdNamesDatabase) : ProdNamesDAO {
        return prodNamesDatabase.customProductNameDao()
    }


    @Provides
    fun provideShopListDAO(prodShopListDatabase : ProdShopListDatabase) : ProdShopListDAO {
        return prodShopListDatabase.customDao()
    }

    @Provides
    fun provideFidCardDAO(fidCardDatabase : FidCardDatabase) : FidCardDao {
        return fidCardDatabase.fidCardNameDao()
    }


}