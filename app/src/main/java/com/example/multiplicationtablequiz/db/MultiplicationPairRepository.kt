package com.example.multiplicationtablequiz.db

import androidx.lifecycle.LiveData
import com.example.multiplicationtablequiz.db.MultiplicationPair
import com.example.multiplicationtablequiz.db.MultiplicationPairDao

class MultiplicationPairRepository (private val multPairDao: MultiplicationPairDao){
    val allPairs: LiveData<List<MultiplicationPair>> = multPairDao.getAll()

    suspend fun insert(multPair: MultiplicationPair){
        multPairDao.insert(multPair)
    }

    suspend fun update(multPair: MultiplicationPair){
        multPairDao.updateMultiplicationPair(multPair)
    }

    suspend fun findByMultPair(prod1 : Int, prod2 : Int) : LiveData<List<MultiplicationPair>>{
        return multPairDao.findByProducts(prod1, prod2)
    }

    suspend fun findAllPairs(): LiveData<List<MultiplicationPair>> {
        return multPairDao.getAll()
    }
}