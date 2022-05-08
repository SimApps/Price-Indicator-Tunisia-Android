package com.amirami.simapp.priceindicatortunisia.fidcard

import androidx.lifecycle.LiveData
import com.amirami.simapp.priceindicatortunisia.fidcard.FidCardEntity


interface FidCardRoomBaseRepository {

    fun giveRepository() : String

    suspend fun  upsert(fidCardEntity : FidCardEntity)

    suspend fun  update(fidCardEntity : FidCardEntity)

    //suspend fun  delete(customEntity : CustomEntity)
    suspend fun  deletebyvalue(value : String)

    suspend fun  deleteAll()

    fun  getAll() : LiveData<List<FidCardEntity>>

}