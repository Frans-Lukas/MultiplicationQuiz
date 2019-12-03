package com.example.multiplicationtablequiz

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.multiplicationtablequiz.db.AppDatabase
import com.example.multiplicationtablequiz.db.MultiplicationPair
import kotlinx.coroutines.*

class QuestionViewModel(application: Application) : AndroidViewModel(application){

    private val repository: MultiplicationPairRepository
    private val viewModelJob = SupervisorJob()
    private val ioScope = CoroutineScope(Dispatchers.IO + viewModelJob)
    val allMultPairs : LiveData<List<MultiplicationPair>>

    init{
        val multPairDao = AppDatabase.getInstance(application).multiplicationPairDao()
        repository = MultiplicationPairRepository(multPairDao)
        allMultPairs = repository.allPairs
    }

    fun insert(multPair: MultiplicationPair) = ioScope.launch {
        repository.insert(multPair)
    }
}