package com.example.dbp_mbg

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dbp_mbg.R

class ItemLogAdapter(private val logList: List<ItemLog>) : RecyclerView.Adapter<ItemLogAdapter.LogViewHolder>() {

    class LogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvDesc: TextView = itemView.findViewById(R.id.tvDesc)
        val tvTime: TextView = itemView.findViewById(R.id.tvTime)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_log_row, parent, false)
        return LogViewHolder(view)
    }

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        val item = logList[position]
        holder.tvTitle.text = item.title
        holder.tvDesc.text = item.description
        holder.tvTime.text = item.time
        holder.tvStatus.text = item.status
    }

    override fun getItemCount(): Int = logList.size
}
