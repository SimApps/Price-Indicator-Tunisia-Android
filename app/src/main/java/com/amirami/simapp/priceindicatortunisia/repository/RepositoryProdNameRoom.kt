package com.amirami.simapp.priceindicatortunisia.repository

import androidx.lifecycle.LiveData
import com.amirami.simapp.priceindicatortunisia.productnames.ProdNamesDAO
import com.amirami.simapp.priceindicatortunisia.productnames.ProdNamesEntity
import javax.inject.Inject

class RepositoryProdNameRoom @Inject constructor(
    private val prodNamesDao : ProdNamesDAO
): ProdNameRoomBaseRepository {


    override fun giveRepository() : String {
        return this.toString()
    }
    //region CRUD Operation
    override suspend fun insert(prodNamesEntity : ProdNamesEntity) {
        prodNamesDao.insert(prodNamesEntity)
    }

    override suspend fun update(prodNamesEntity : ProdNamesEntity) {
        prodNamesDao.update(
            prodNamesEntity
        )
    }

    /*override suspend fun delete(customEntity: CustomEntity) {
        println("${customEntity.name}")
        customDao.delete(
            customEntity.name
        )
    }*/

    override suspend fun delete(prodname: String) {
        prodNamesDao.delete(prodname)
    }

    override suspend fun deleteAll() {
        prodNamesDao.deleteAll()
    }

    override fun getAll() : LiveData<List<ProdNamesEntity>> {
        return prodNamesDao.getAll()
    }
    //endregion
}