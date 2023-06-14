package com.amirami.simapp.priceindicatortunisia.cartefidelite.firestore.di

import com.amirami.simapp.priceindicatortunisia.cartefidelite.firestore.repository.CartFidRepository
import com.amirami.simapp.priceindicatortunisia.cartefidelite.firestore.repository.CartFidRepositoryImpl
import com.amirami.simapp.priceindicatortunisia.cartefidelite.firestore.usecases.AddListFidCard
import com.amirami.simapp.priceindicatortunisia.cartefidelite.firestore.usecases.DeleteFidCard
import com.amirami.simapp.priceindicatortunisia.cartefidelite.firestore.usecases.GetFidCard
import com.amirami.simapp.priceindicatortunisia.cartefidelite.firestore.usecases.UseCasesFidCard
import com.amirami.simapp.priceindicatortunisia.core.Constants
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
object AppModuleFidCard {

    @Provides
    @Singleton
    @Named("Fid_Card")
    fun provideFidCardRef(
        db: FirebaseFirestore
    ) = db.collection(Constants.FID_CARD_LIST_COLLECTION)

    @Provides
    @Singleton
    fun provideFidCardRepository(
        @Named("Fid_Card")     fidCardRef: CollectionReference
    ): CartFidRepository = CartFidRepositoryImpl(fidCardRef)

    @Provides
    @Singleton
    fun provideUseCases(
        repo: CartFidRepository
    ) = UseCasesFidCard(
        getFidCard = GetFidCard(repo),
        addListFidCard = AddListFidCard(repo),
        deleteFidCard = DeleteFidCard(repo),
    )
}