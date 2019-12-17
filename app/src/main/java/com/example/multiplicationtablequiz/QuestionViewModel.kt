package com.example.multiplicationtablequiz

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.multiplicationtablequiz.db.MultiplicationPairDatabase
import com.example.multiplicationtablequiz.db.MultiplicationPair
import com.example.multiplicationtablequiz.db.MultiplicationPairRepository
import kotlinx.coroutines.*

class QuestionViewModel(application: Application) : AndroidViewModel(application){

    private val repository: MultiplicationPairRepository
    private val viewModelJob = SupervisorJob()
    private val ioScope = CoroutineScope(Dispatchers.IO + viewModelJob)
    val allMultPairs : LiveData<List<MultiplicationPair>>

    init{
        val multPairDao = MultiplicationPairDatabase.getInstance(application, ioScope).multiplicationPairDao()
        repository = MultiplicationPairRepository(multPairDao)
        allMultPairs = repository.allPairs
    }

    fun insert(multPair: MultiplicationPair) = ioScope.launch {
        repository.insert(multPair)
    }

    fun findByPair(prod1:Int, prod2:Int) : LiveData<List<MultiplicationPair>> =
            runBlocking { repository.findByMultPair(prod1, prod2) }


    fun update(multPair: MultiplicationPair) =
        ioScope.launch{ repository.update(multPair) }


}