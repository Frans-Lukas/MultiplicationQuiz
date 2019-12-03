package com.example.multiplicationtablequiz

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.multiplicationtablequiz.db.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class StatsActivity : Activity(), CoroutineScope{
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)
        launch{
            loadFailingPairsToViewAdapter(applicationContext)
        }


    }

    suspend fun loadFailingPairsToViewAdapter(context: Context){
        val db = AppDatabase.getInstance(applicationContext)
        withContext(Dispatchers.IO){
            val failingPairs = db.multiplicationPairDao().getAll()
                    .filter { mp -> mp.numWrong >= mp.numCorrect }
                    .map { mp -> mp.firstProduct.toString() + "x" + mp.secondProduct.toString()}
            withContext(Dispatchers.Main){
                viewManager = LinearLayoutManager(context)
                viewAdapter = StatsAdapter(failingPairs.toTypedArray())
                recyclerView = findViewById<RecyclerView>(R.id.recyclerViewStats).apply {
                    setHasFixedSize(true)
                    layoutManager = viewManager
                    adapter = viewAdapter
                }
            }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
}