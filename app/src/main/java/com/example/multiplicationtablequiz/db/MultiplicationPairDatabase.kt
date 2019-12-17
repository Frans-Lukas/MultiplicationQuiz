package com.example.multiplicationtablequiz.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = arrayOf(MultiplicationPair::class), version = 1, exportSchema=false)
abstract class MultiplicationPairDatabase : RoomDatabase(){
    abstract fun multiplicationPairDao(): MultiplicationPairDao

    companion object{
        @Volatile private var INSTANCE: MultiplicationPairDatabase? = null

        fun getInstance(context: Context,
                        scope: CoroutineScope): MultiplicationPairDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        MultiplicationPairDatabase::class.java,
                        "word_database"
                ).addCallback(MultiplicationPairDatabaseCallback(scope)).build()
                INSTANCE = instance
                return instance
            }
        }
    }

    private class MultiplicationPairDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback(){
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database -> scope.launch { populateDatabase(database.multiplicationPairDao()) } }
        }

        suspend fun populateDatabase(multPairDao: MultiplicationPairDao){
            //multPairDao.deleteAll()

            var pair = MultiplicationPair( 15, 10, 50, 1000)
            multPairDao.insert(pair)

            val pair2 = MultiplicationPair( 20, 10, 100, 1000)
            multPairDao.insert(pair2)
        }
    }
}
