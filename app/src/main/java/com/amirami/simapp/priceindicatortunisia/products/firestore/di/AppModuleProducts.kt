package com.amirami.simapp.priceindicatortunisia.products.firestore.di

import com.amirami.simapp.priceindicatortunisia.core.Constants.PRODUCTS_COLLECTION
import com.amirami.simapp.priceindicatortunisia.products.firestore.data.repository.ProductsRepositoryImpl
import com.amirami.simapp.priceindicatortunisia.products.firestore.domain.repository.ProductsRepository
import com.amirami.simapp.priceindicatortunisia.products.firestore.domain.use_case.*
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModuleProducts {

    @Provides
    @Singleton
    @Named("prod")
    fun provideProductsRef(
        db: FirebaseFirestore
    ) = db.collection(PRODUCTS_COLLECTION)

    @Provides
    @Singleton
    fun provideProductsRepository(
        @Named("prod")  booksRef: CollectionReference
    ): ProductsRepository = ProductsRepositoryImpl(booksRef)

    @Provides
    @Singleton
    fun provideUseCases(
        repo: ProductsRepository
    ) = UseCasesProduct(
        getProduct = GetProduct(repo),
        addProduct = AddProduct(repo),
        addProducts = AddProducts(repo),
        deleteProduct = DeleteProduct(repo)
    )
}