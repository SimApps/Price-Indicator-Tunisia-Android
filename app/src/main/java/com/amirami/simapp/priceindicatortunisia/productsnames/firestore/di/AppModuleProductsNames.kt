package com.amirami.simapp.priceindicatortunisia.productsnames.firestore.di

import com.amirami.simapp.priceindicatortunisia.core.Constants.PRODUCTS_LIST_NAMES_COLLECTION
import com.amirami.simapp.priceindicatortunisia.productsnames.firestore.data.repository.ProductsNamesRepositoryImpl
import com.amirami.simapp.priceindicatortunisia.productsnames.firestore.domain.repository.ProductsNamesRepository
import com.amirami.simapp.priceindicatortunisia.productsnames.firestore.domain.use_case.*
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
        getProductsNamesBareCode = GetProductsNamesBareCode(repo),
        addProductName = AddProductName(repo),
        addListProductsNames = AddListProductsNames(repo),
        deleteProductName = DeleteProductName(repo),
        addListProductsNamesBareCode = AddListProductsNamesBarCode(repo)
    )
}
