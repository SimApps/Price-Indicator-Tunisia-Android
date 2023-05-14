package com.amirami.simapp.priceindicatortunisia.core.local_db.di

import android.content.Context
import androidx.room.Room
import com.amirami.simapp.priceindicatortunisia.core.local_db.db.LocalDb
import com.amirami.simapp.priceindicatortunisia.productsnames.room.core.Constants.Companion.PRODUCTS_NAMES_TABLE
import com.amirami.simapp.priceindicatortunisia.productsnames.room.data.dao.ProdNamesDAO
import com.amirami.simapp.priceindicatortunisia.productsnames.room.data.repository.NameListRepositoryImpl
import com.amirami.simapp.priceindicatortunisia.productsnames.room.domain.repository.NameListRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
class NameListModule {
    @Provides
    @Named("ProductNameListDB")

    fun provideListNameDb(
        @ApplicationContext
        context: Context
    ) = Room.databaseBuilder(
        context,
        LocalDb::class.java,
        PRODUCTS_NAMES_TABLE,
    ).build()




    @Provides
    fun provideNameListDao(
        @Named("ProductNameListDB")  bookDb: LocalDb
    ) = bookDb.bookDao()
    @Provides
    fun provideListNameRepository(
        prodNamesDAO: ProdNamesDAO
    ): NameListRepository = NameListRepositoryImpl(
        prodNamesDAO = prodNamesDAO,
    )








}
