package com.amirami.simapp.priceindicatortunisia.productsnames.firestore.di

import com.amirami.simapp.priceindicatortunisia.core.Constants.PRODUCTS_LIST_NAMES_COLLECTION
import com.amirami.simapp.priceindicatortunisia.productsnames.firestore.repository.ProductsNamesRemoteRepository
import com.amirami.simapp.priceindicatortunisia.productsnames.firestore.repository.ProductsNamesRemoteRepositoryImpl
import com.amirami.simapp.priceindicatortunisia.productsnames.firestore.use_case.AddListProductsNamesBarCode
import com.amirami.simapp.priceindicatortunisia.productsnames.firestore.use_case.DeleteProductName
import com.amirami.simapp.priceindicatortunisia.productsnames.firestore.use_case.GetProductsNamesBareCode
import com.amirami.simapp.priceindicatortunisia.productsnames.firestore.use_case.UseCasesProductName
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
object AppModuleProductsNames {

    @Provides
    @Singleton
    @Named("Product_Name")
    fun provideProductsNamesRef(
        db: FirebaseFirestore
    ) = db.collection(PRODUCTS_LIST_NAMES_COLLECTION)

    @Provides
    @Singleton
    fun provideProductsNamesRepository(
        @Named("Product_Name")   booksRef: CollectionReference
    ): ProductsNamesRemoteRepository = ProductsNamesRemoteRepositoryImpl(booksRef)

    @Provides
    @Singleton
    fun provideUseCases(
        repo: ProductsNamesRemoteRepository
    ) = UseCasesProductName(
        getProductsNamesBareCode = GetProductsNamesBareCode(repo),
        deleteProductName = DeleteProductName(repo),
        addListProductsNamesBareCode = AddListProductsNamesBarCode(repo)
    )
}
