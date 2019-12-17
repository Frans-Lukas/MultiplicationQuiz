package com.example.multiplicationtablequiz.activity

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.multiplicationtablequiz.QuestionViewModel
import com.example.multiplicationtablequiz.R
import com.example.multiplicationtablequiz.StatsAdapter

class StatsActivity : AppCompatActivity(){
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: StatsAdapter
    private lateinit var questionViewModel: QuestionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)
        recyclerView = findViewById(R.id.recyclerViewStats)
        viewAdapter = StatsAdapter(this)
        recyclerView.adapter = viewAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        questionViewModel = ViewModelProvider(this).get(QuestionViewModel::class.java)
        questionViewModel.allMultPairs.observe(this, Observer { pairs -> pairs?.let{ viewAdapter.setPairs(it)} })

    }

}