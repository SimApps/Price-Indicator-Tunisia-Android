package com.amirami.simapp.priceindicatortunisia.products.firestore.di

import com.amirami.simapp.priceindicatortunisia.core.Constants.PRODUCTS_COLLECTION
import com.amirami.simapp.priceindicatortunisia.products.firestore.data.repository.ProductsRepositoryImpl
import com.amirami.simapp.priceindicatortunisia.products.firestore.domain.repository.ProductsRepository
import com.amirami.simapp.priceindicatortunisia.products.firestore.domain.usecases.*
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

   /* @Provides
    fun provideQueryProductsByName(searchtext : String) = Firebase.firestore
        .collection(PRODUCTS_COLLECTION)
       .whereEqualTo(
            Functions.searchtype,
            searchtext
        )
        //  .orderBy(NAME_PROPERTY, Query.Direction.ASCENDING)
        .limit(PAGE_SIZE.toLong())
    @Provides
    fun provideProductsPagingSource(
        queryProductsByName: Query
    ) = ProductsPagingSource(
        queryProductsByName = queryProductsByName
    )

    @Provides
    fun providePagingConfig() = PagingConfig(
        pageSize = PAGE_SIZE
    )*/
    @Provides
    @Singleton
    fun provideProductsRepository(
        @Named("prod")  booksRef: CollectionReference/*,
        source: ProductsPagingSource,
        config: PagingConfig*/
    ): ProductsRepository = ProductsRepositoryImpl(
        collectionRef = booksRef/*,
        source = source,
        config = config*/
    )

    @Provides
    @Singleton
    fun provideUseCases(
        repo: ProductsRepository
    ) = UseCasesProduct(
        getProduct = GetProduct(repo),
        addProduct = AddProduct(repo),
        addProducts = AddProducts(repo),
        deleteProduct = DeleteProduct(repo),
        getAllProduct = GetAllProduct(repo)
    )
}