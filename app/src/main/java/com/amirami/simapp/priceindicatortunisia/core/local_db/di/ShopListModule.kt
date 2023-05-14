package com.amirami.simapp.priceindicatortunisia.core.local_db.di

import android.content.Context
import androidx.room.Room
import com.amirami.simapp.priceindicatortunisia.core.local_db.db.LocalDb
import com.amirami.simapp.priceindicatortunisia.products.room.core.Constants
import com.amirami.simapp.priceindicatortunisia.products.room.data.dao.ProdShopListDAO
import com.amirami.simapp.priceindicatortunisia.products.room.data.repository.ShopListRepositoryImpl
import com.amirami.simapp.priceindicatortunisia.products.room.domain.repository.ShopListRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named


@Module
    @InstallIn(SingletonComponent::class)
    class ShopListModule {
        @Provides
        @Named("ShopListDB")
        fun provideShopListDb(
            @ApplicationContext
            context: Context
        ) = Room.databaseBuilder(
            context,
            LocalDb::class.java,
            Constants.SHOPING_LIST_TABLE,
        ).build()

        @Provides
        fun provideShopListDao(
            @Named("ShopListDB")     bookDb: LocalDb
        ) = bookDb.prodShopListDAO()

        @Provides
        fun provideShopListRepository(
            prodNamesDAO: ProdShopListDAO
        ): ShopListRepository = ShopListRepositoryImpl(
            prodShopListDAO = prodNamesDAO,
        )
}