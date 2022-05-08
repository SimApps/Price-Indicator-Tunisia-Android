package com.amirami.simapp.priceindicatortunisia.shoping



import androidx.lifecycle.LiveData
import com.amirami.simapp.priceindicatortunisia.repository.ShopListRoomBaseRepository
import javax.inject.Inject

class RepositoryShopListRoom @Inject constructor(
    private val prodShopListDAO : ProdShopListDAO
): ShopListRoomBaseRepository {



    override fun giveRepository() : String {
        return this.toString()
    }



    //region CRUD Operation
    override suspend fun insert(prodShopListEntity : ProdShopListEntity) {
        prodShopListDAO.insert(
            prodShopListEntity
        )
    }

    override suspend fun update(prodShopListEntity : ProdShopListEntity) {
        prodShopListDAO.update(
            prodShopListEntity
        )
    }

    override suspend fun updateQuantity(quantity : Double,id:Long) {
        prodShopListDAO.updateQuantity(
            quantity,id
        )
    }


    /*override suspend fun delete(customEntity: CustomEntity) {
        println("${customEntity.name}")
        customDao.delete(
            customEntity.name
        )
    }*/

    override suspend fun delete(productid: Long?) {
        prodShopListDAO.delete(
            productid
        )
    }

    override suspend fun deleteAll() {
        prodShopListDAO.deleteAll()
    }

    override fun getAll() : LiveData<List<ProdShopListEntity>> {
        return prodShopListDAO.getAll()
    }
    //endregion
}