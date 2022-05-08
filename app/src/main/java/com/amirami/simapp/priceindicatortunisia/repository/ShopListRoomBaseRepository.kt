package com.amirami.simapp.priceindicatortunisia.repository

import androidx.lifecycle.LiveData
import com.amirami.simapp.priceindicatortunisia.shoping.ProdShopListEntity

interface ShopListRoomBaseRepository {

    fun giveRepository() : String

    suspend fun  insert(prodShopListEntity : ProdShopListEntity)

    suspend fun  update(prodShopListEntity : ProdShopListEntity)

    suspend fun  updateQuantity(quantity : Double,id:Long)


    //suspend fun  delete(customEntity : CustomEntity)
    suspend fun  delete(prodId : Long?)

    suspend fun  deleteAll()

    fun  getAll() : LiveData<List<ProdShopListEntity>>


}
