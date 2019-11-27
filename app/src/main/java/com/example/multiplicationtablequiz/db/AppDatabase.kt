package com.example.multiplicationtablequiz.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(MultiplicationPair::class), version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun multiplicationPairDao(): MultiplicationPairDao

    companion object{
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "multiplication-pairs-db"
        ).build()
    }
}