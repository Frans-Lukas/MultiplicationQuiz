package com.example.multiplicationtablequiz

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.multiplicationtablequiz.db.MultiplicationPair

class StatsAdapter internal constructor(context: Context) :
        RecyclerView.Adapter<StatsAdapter.PairViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var pairs = emptyList<MultiplicationPair>()

    inner class PairViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pairItemView: TextView = itemView.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PairViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return PairViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PairViewHolder, position: Int) {
        val current = pairs[position]
        val pair = current.firstProduct.toString() + "x" + current.secondProduct.toString()
        holder.pairItemView.text = pair
    }

    internal fun setPairs(pairs: List<MultiplicationPair>){
        this.pairs = pairs
        notifyDataSetChanged()
    }

    override fun getItemCount() = pairs.size
}