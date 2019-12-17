package com.example.multiplicationtablequiz.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MultiplicationPair(
        @ColumnInfo(name = "firstProduct") val firstProduct: Int,
        @ColumnInfo(name = "secondProduct") val secondProduct: Int,
        @ColumnInfo(name = "numCorrect") val numCorrect: Int,
        @ColumnInfo(name = "numWrong") val numWrong: Int
){
    @PrimaryKey(autoGenerate = true) var uid: Int = 0
}