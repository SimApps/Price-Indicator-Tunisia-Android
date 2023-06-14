package com.amirami.simapp.priceindicatortunisia.cartefidelite.room.data.repository

import com.amirami.simapp.priceindicatortunisia.cartefidelite.room.data.network.FidCardDao
import com.amirami.simapp.priceindicatortunisia.cartefidelite.room.domain.model.FidCardEntity
import com.amirami.simapp.priceindicatortunisia.cartefidelite.room.domain.repository.FidCardRoomBaseRepository
import kotlinx.coroutines.flow.Flow

class BookRepositoryImpl(
    private val fidCardDao: FidCardDao
) : FidCardRoomBaseRepository {

    override fun upsert(fidCardEntity: FidCardEntity) = fidCardDao.upsert(fidCardEntity)


    override fun deletebyid(value: String) = fidCardDao.deleteFidCardById(value)

    override fun deleteAll()= fidCardDao.deleteAll()

    override fun getAll(): Flow<List<FidCardEntity>> = fidCardDao.get()
    override fun getByValue(value: String): Flow<FidCardEntity?> = fidCardDao.getByValue(value)



}