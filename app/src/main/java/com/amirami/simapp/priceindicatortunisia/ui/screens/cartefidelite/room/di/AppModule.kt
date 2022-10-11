package com.amirami.simapp.priceindicatortunisia.ui.screens.cartefidelite.room.di

import android.content.Context
import androidx.room.Room
import com.amirami.simapp.priceindicatortunisia.ui.screens.cartefidelite.room.core.Constants.Companion.FIDCARD_TABLE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.amirami.simapp.priceindicatortunisia.ui.screens.cartefidelite.room.data.network.FidCardDao
import com.amirami.simapp.priceindicatortunisia.ui.screens.cartefidelite.room.data.network.FidCardDatabase
import com.amirami.simapp.priceindicatortunisia.ui.screens.cartefidelite.room.data.repository.BookRepositoryImpl
import com.amirami.simapp.priceindicatortunisia.ui.screens.cartefidelite.room.domain.repository.FidCardRoomBaseRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun provideBookDb(
        @ApplicationContext
        context : Context
    ) = Room.databaseBuilder(
        context,
        FidCardDatabase::class.java,
        FIDCARD_TABLE
    ).build()

    @Provides
    fun provideBookDao(
        bookDb: FidCardDatabase
    ) = bookDb.fidCardNameDao()

    @Provides
    fun provideBookRepository(
        prodNamesDAO: FidCardDao
    ): FidCardRoomBaseRepository = BookRepositoryImpl(
        fidCardDao= prodNamesDAO

    )

}





