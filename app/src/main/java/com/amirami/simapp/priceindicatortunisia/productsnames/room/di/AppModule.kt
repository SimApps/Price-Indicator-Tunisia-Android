package com.amirami.simapp.priceindicatortunisia.productsnames.room.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.amirami.simapp.priceindicatortunisia.productsnames.room.core.Constants.Companion.PRODUCTS_NAMES_TABLE
import com.amirami.simapp.priceindicatortunisia.productsnames.room.data.network.BookDb
import com.amirami.simapp.priceindicatortunisia.productsnames.room.data.network.ProdNamesDAO
import com.amirami.simapp.priceindicatortunisia.productsnames.room.data.repository.BookRepositoryImpl
import com.amirami.simapp.priceindicatortunisia.productsnames.room.domain.repository.BookRepository

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun provideBookDb(
        @ApplicationContext
        context : Context
    ) = Room.databaseBuilder(
        context,
        BookDb::class.java,
        PRODUCTS_NAMES_TABLE
    ).build()

    @Provides
    fun provideBookDao(
        bookDb: BookDb
    ) = bookDb.bookDao()

    @Provides
    fun provideBookRepository(
        prodNamesDAO: ProdNamesDAO
    ): BookRepository = BookRepositoryImpl(
        prodNamesDAO = prodNamesDAO
    )
}