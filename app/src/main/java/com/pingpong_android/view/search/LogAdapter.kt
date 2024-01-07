package com.pingpong_android.view.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pingpong_android.databinding.ItemFriendListBinding
import com.pingpong_android.model.LogDTO

class LogAdapter(private var logList : List<LogDTO>) : RecyclerView.Adapter<LogAdapter.LogViewHolder>() {

    private lateinit var activity: SearchActivity

    fun setActivity(activity: SearchActivity) {
        this.activity = activity
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogAdapter.LogViewHolder {
        val binding = ItemFriendListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LogAdapter.LogViewHolder, position: Int) {
        holder.bind(logList.get(position), position)
        holder.itemView.setOnClickListener{v -> activity.goToUserProfile(logList.get(position).memberId)}
    }

    override fun getItemCount(): Int {
        return logList.size
    }

    inner class LogViewHolder(binding : ItemFriendListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(logDTO: LogDTO, position: Int) {

        }
    }
}