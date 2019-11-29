package com.example.multiplicationtablequiz.db

import androidx.room.*

@Dao
interface MultiplicationPairDao {

    @Query ("SELECT * FROM multiplicationpair")
    fun getAll(): List<MultiplicationPair>

    @Query ("SELECT * FROM multiplicationpair WHERE firstProduct=:first AND secondProduct=:second")
    fun findByProducts(first: Int, second: Int): MultiplicationPair

    @Insert
    fun insertAll(vararg multiplicationPairs: MultiplicationPair)

    @Update
    fun updateMultiplicationPair(pair: MultiplicationPair)

    @Delete
    fun delete(multiplicationPair: MultiplicationPair)

}
