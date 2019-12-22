package com.example.multiplicationtablequiz.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MultiplicationPairDao {

    @Query ("SELECT * FROM multiplicationpair")
    fun getAll(): LiveData<List<MultiplicationPair>>

    @Query ("SELECT * FROM multiplicationpair ORDER BY -(numCorrect - numWrong) LIMIT 50")
    fun getTop50WorstPerformance(): LiveData<List<MultiplicationPair>>

    @Query ("SELECT * FROM multiplicationpair WHERE firstProduct=:first AND secondProduct=:second")
    suspend fun findByProducts(first: Int, second: Int): List<MultiplicationPair>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg multiplicationPairs: MultiplicationPair)

    @Update
    suspend fun updateMultiplicationPair(pair: MultiplicationPair)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(multiplicationPair: MultiplicationPair)

    @Delete
    suspend fun delete(multiplicationPair: MultiplicationPair)

    @Query("DELETE FROM multiplicationpair")
    suspend fun deleteAll()

}
