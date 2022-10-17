package com.amirami.simapp.priceindicatortunisia.productsnames.firestore.di

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.amirami.simapp.priceindicatortunisia.productsnames.firestore.data.repository.ProductsNamesRepositoryImpl
import com.amirami.simapp.priceindicatortunisia.productsnames.firestore.domain.repository.ProductsNamesRepository
import com.amirami.simapp.priceindicatortunisia.core.Constants.PRODUCTS_LIST_NAMES_COLLECTION
import com.amirami.simapp.priceindicatortunisia.productsnames.firestore.domain.use_case.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModuleProductsNames {

    @Provides
    @Singleton
    fun provideProductsNamesRef(
        db: FirebaseFirestore
    ) = db.collection(PRODUCTS_LIST_NAMES_COLLECTION)

    @Provides
    @Singleton
    fun provideProductsNamesRepository(
        booksRef: CollectionReference
    ): ProductsNamesRepository = ProductsNamesRepositoryImpl(booksRef)

    @Provides
    @Singleton
    fun provideUseCases(
        repo: ProductsNamesRepository
    ) = UseCasesProductName(
        getProductsNames = GetProductsNames(repo),
        addProductName = AddProductName(repo),
        addProductsNames = AddProductsNames(repo),
        deleteProductName = DeleteProductName(repo)
    )
}