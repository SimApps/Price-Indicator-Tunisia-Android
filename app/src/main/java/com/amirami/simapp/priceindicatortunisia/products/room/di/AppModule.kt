package com.amirami.simapp.priceindicatortunisia.products.room.di

import android.content.Context
import androidx.room.Room
import com.amirami.simapp.priceindicatortunisia.products.room.core.Constants.Companion.SHOPING_LIST_TABLE
import com.amirami.simapp.priceindicatortunisia.products.room.data.network.ProdShopListDAO
import com.amirami.simapp.priceindicatortunisia.products.room.data.network.ProdShopListDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.amirami.simapp.priceindicatortunisia.products.room.data.repository.ShopListRepositoryImpl
import com.amirami.simapp.priceindicatortunisia.products.room.domain.repository.ShopListRepository

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun provideShopListDb(
        @ApplicationContext
        context : Context
    ) = Room.databaseBuilder(
        context,
        ProdShopListDb::class.java,
        SHOPING_LIST_TABLE
    ).build()

    @Provides
    fun provideBookDao(
        bookDb: ProdShopListDb
    ) = bookDb.prodShopListDAO()

    @Provides
    fun provideBookRepository(
        prodNamesDAO: ProdShopListDAO
    ): ShopListRepository = ShopListRepositoryImpl(
        prodShopListDAO = prodNamesDAO
    )
}