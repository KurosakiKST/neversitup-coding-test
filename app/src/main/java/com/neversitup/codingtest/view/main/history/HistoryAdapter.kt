package com.neversitup.codingtest.view.main.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.neversitup.codingtest.databinding.ItemHistoryBinding
import com.neversitup.codingtest.model.local.database.entity.Record

class HistoryAdapter(
        private val records : List<Record>
) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    class ViewHolder(view : ItemHistoryBinding) : RecyclerView.ViewHolder(view.root) {
        val tvDateTime = view.tvDateTime
        val tvUsd = view.tvUsd
        val tvGbp = view.tvGbp
        val tvEuro = view.tvEuro
    }

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : ViewHolder {
        val binding : ItemHistoryBinding = ItemHistoryBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder : ViewHolder, position : Int) {
        val item = records.get(position)
        holder.tvDateTime.text = item.timestamp
        holder.tvUsd.text = item.usdValue
        holder.tvGbp.text = item.gbpValue
        holder.tvEuro.text = item.eurValue
    }

    override fun getItemCount() : Int = records.size
}