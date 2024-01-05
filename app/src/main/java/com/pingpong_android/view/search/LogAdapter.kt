package com.pingpong_android.view.search

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pingpong_android.databinding.ItemFriendListBinding
import com.pingpong_android.model.LogDTO

class LogAdapter(logList : List<LogDTO>) : RecyclerView.Adapter<LogAdapter.LogViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogAdapter.LogViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: LogAdapter.LogViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    inner class LogViewHolder(binding : ItemFriendListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(logDTO: LogDTO, position: Int) {

        }
    }
}