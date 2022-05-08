package com.amirami.simapp.priceindicatortunisia.repository

import androidx.lifecycle.LiveData
import com.amirami.simapp.priceindicatortunisia.productnames.ProdNamesEntity

interface ProdNameRoomBaseRepository {

    fun giveRepository() : String

    suspend fun  insert(prodNamesEntity : ProdNamesEntity)

    suspend fun  update(prodNamesEntity : ProdNamesEntity)

    //suspend fun  delete(customEntity : CustomEntity)
    suspend fun  delete(prodName : String)

    suspend fun  deleteAll()

    fun  getAll() : LiveData<List<ProdNamesEntity>>

}