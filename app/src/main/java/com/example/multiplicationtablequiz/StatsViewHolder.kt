package com.example.multiplicationtablequiz

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.multiplicationtablequiz.db.MultiplicationPair

class StatsViewHolder(inflater: LayoutInflater, parent: ViewGroup):
        RecyclerView.ViewHolder(inflater.inflate(R.layout.text_view_stats, parent, false)){
    private lateinit var textViewPair : TextView
    private lateinit var textViewScore : TextView

    init {
        textViewPair = itemView.findViewById(R.id.textViewPair)
        textViewScore = itemView.findViewById(R.id.textViewScore)
    }

    fun bind(multPair: MultiplicationPair){
        val pair = multPair.firstProduct.toString() + "x" + multPair.secondProduct.toString()
        val score = (multPair.numCorrect.toFloat() / (multPair.numCorrect + multPair.numWrong).toFloat() * 100).toInt().toString() + "%"
        textViewPair.text = pair
        textViewScore.text = score

    }
}