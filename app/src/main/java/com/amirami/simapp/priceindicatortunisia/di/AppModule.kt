package com.amirami.simapp.priceindicatortunisia.di

import android.nfc.tech.MifareUltralight.PAGE_SIZE
import com.amirami.simapp.priceindicatortunisia.repository.*
import com.amirami.simapp.priceindicatortunisia.fidcard.FidCardDao
import com.amirami.simapp.priceindicatortunisia.fidcard.FidCardRoom
import com.amirami.simapp.priceindicatortunisia.fidcard.FidCardRoomBaseRepository
import com.amirami.simapp.priceindicatortunisia.productnames.ProdNamesDAO
import com.amirami.simapp.priceindicatortunisia.shoping.ProdShopListDAO
import com.amirami.simapp.priceindicatortunisia.shoping.RepositoryShopListRoom
import com.amirami.simapp.priceindicatortunisia.utils.Constants.PRODUCTS_COLLECTION
import com.amirami.simapp.priceindicatortunisia.utils.Constants.USERS_IDS_REF
import com.amirami.simapp.priceindicatortunisia.utils.Functions
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideFirebaseFirestore() = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseRealtimeDatabase() = FirebaseDatabase.getInstance()

    @Provides
    @Singleton
    fun provideProductsCollectionReference(rootRef: FirebaseFirestore) = rootRef.collection(PRODUCTS_COLLECTION)

    @Provides
    @Singleton
    fun provideQueryProductsByName(rootRef: FirebaseFirestore) = rootRef.collection(PRODUCTS_COLLECTION)

        .whereEqualTo(
            Functions.searchtype,
            Functions.searchtext
        )
        //  .orderBy(NAME_PROPERTY, Query.Direction.ASCENDING)
        .limit(PAGE_SIZE.toLong())


    @Provides
    @Singleton
    fun provideUsersIDsDatabaseReference(idRef: FirebaseDatabase) = idRef.getReference(USERS_IDS_REF)




    @Provides
    @Singleton
     fun provideProdNamesListRepository(prodNamesDao : ProdNamesDAO) : ProdNameRoomBaseRepository {
        return RepositoryProdNameRoom(prodNamesDao)
    }

    @Provides
    @Singleton
    fun provideShopingListRepository(provideShopListDAO : ProdShopListDAO) : ShopListRoomBaseRepository {
        return RepositoryShopListRoom(provideShopListDAO)
    }

    @Provides
    @Singleton
    fun provideFidCardRepository(provideFidCardDAO : FidCardDao) : FidCardRoomBaseRepository {
        return FidCardRoom(provideFidCardDAO)
    }





    /*
    @Retention(AnnotationRetention.RUNTIME)
    @Qualifier
    annotation class ApplicationScope
    */

}