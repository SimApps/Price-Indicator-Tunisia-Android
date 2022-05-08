package com.amirami.simapp.priceindicatortunisia.fidcard

import androidx.lifecycle.LiveData
import javax.inject.Inject


class FidCardRoom @Inject constructor(
    private val fidCardDao : FidCardDao
): FidCardRoomBaseRepository {

    override fun giveRepository(): String {
        return this.toString()
    }

    override suspend fun upsert(fidCardEntity: FidCardEntity) {
        fidCardDao.upsert(fidCardEntity)
    }

    override suspend fun update(fidCardEntity: FidCardEntity) {
        TODO("Not yet implemented")
    }


    override suspend fun deletebyvalue(value: String) {
        fidCardDao.deleteFidCardById(value)
    }

    override suspend fun deleteAll() {
        fidCardDao.deleteAll()
    }

    override fun getAll(): LiveData<List<FidCardEntity>> {
        return fidCardDao.get()
    }


}