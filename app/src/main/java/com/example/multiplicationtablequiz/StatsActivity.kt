package com.example.multiplicationtablequiz

import android.app.Activity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.multiplicationtablequiz.db.AppDatabase
import com.example.multiplicationtablequiz.db.MultiplicationPair

class StatsActivity : Activity(){
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)

        viewManager = LinearLayoutManager(this)
        val db = AppDatabase.getInstance(applicationContext)
        val results:List<String> = db.multiplicationPairDao().getAll()
                .filter { mp -> mp.numWrong >= mp.numCorrect }
                .map { mp -> mp.firstProduct.toString() + "x" + mp.secondProduct.toString()}
        viewAdapter = StatsAdapter(results.toTypedArray())
        recyclerView = findViewById<RecyclerView>(R.id.recyclerViewStats).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

    }
}