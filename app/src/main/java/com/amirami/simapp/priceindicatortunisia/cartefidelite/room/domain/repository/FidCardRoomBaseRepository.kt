package com.amirami.simapp.priceindicatortunisia.cartefidelite.room.domain.repository

import com.amirami.simapp.priceindicatortunisia.cartefidelite.room.domain.model.FidCardEntity
import kotlinx.coroutines.flow.Flow


interface FidCardRoomBaseRepository {


     fun  upsert(fidCardEntity : FidCardEntity) //: Long


    //suspend fun  delete(customEntity : CustomEntity)
     fun  deletebyid(value : String)

     fun  deleteAll()

    fun  getAll() : Flow<List<FidCardEntity>>

    fun  getByValue(value: String) : Flow<FidCardEntity?>
}