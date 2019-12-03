package com.example.multiplicationtablequiz

import androidx.lifecycle.LiveData
import com.example.multiplicationtablequiz.db.MultiplicationPair
import com.example.multiplicationtablequiz.db.MultiplicationPairDao

class MultiplicationPairRepository (private val multPairDao: MultiplicationPairDao){
    val allPairs: LiveData<List<MultiplicationPair>> = multPairDao.getAll()

    suspend fun insert(multPair: MultiplicationPair){
        multPairDao.insert(multPair)
    }
}