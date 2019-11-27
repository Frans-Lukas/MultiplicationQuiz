package com.example.multiplicationtablequiz.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MultiplicationPairDao {

    @Query ("SELECT * FROM multiplicationPair")
    fun getAll(): List<MultiplicationPair>

    @Insert
    fun insertAll(vararg multiplicationPairs: MultiplicationPair)

    @Delete
    fun delete(multiplicationPair: MultiplicationPair)

}
