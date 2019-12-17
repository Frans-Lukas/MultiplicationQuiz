package com.example.multiplicationtablequiz

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.multiplicationtablequiz.db.MultiplicationPair

class StatsAdapter internal constructor(context: Context) :
        RecyclerView.Adapter<StatsViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var pairs = emptyList<MultiplicationPair>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatsViewHolder {
        return StatsViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: StatsViewHolder, position: Int) {
        val current = pairs[position]
        holder.bind(current)
    }

    internal fun setPairs(pairs: List<MultiplicationPair>){
        this.pairs = pairs
        notifyDataSetChanged()
    }

    override fun getItemCount() = pairs.size
}