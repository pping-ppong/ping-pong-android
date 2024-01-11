package com.pingpong_android.view.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pingpong_android.R
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
    }

    override fun getItemCount(): Int {
        return logList.size
    }

    fun addList(logList : List<LogDTO>) {
        this.logList = logList
        notifyDataSetChanged()
    }

    inner class LogViewHolder(val binding : ItemFriendListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(logDTO: LogDTO, position: Int) {
            if (logDTO.keyword != null && logDTO.keyword.isNotEmpty()) {
                binding.icSearch.visibility = View.VISIBLE
                binding.defaultImage.visibility = View.GONE
                binding.nickNmEt.text = logDTO.keyword

                binding.itemView.setOnClickListener { activity.searchKeyword(logDTO.keyword) }
            } else {
                binding.icSearch.visibility = View.GONE
                binding.image.visibility = View.VISIBLE
                binding.nickNmEt.text = logDTO.nickName

                if (logDTO.profileImage.isNotEmpty()) {
                    binding.defaultImage.visibility = View.GONE
                    Glide.with(binding.profileImg).load(logDTO.profileImage)
                        .error(R.drawable.ic_profile_popcorn)   // 오류일 경우
                        .fallback(R.drawable.ic_profile_popcorn)    // Null인 경우
                        .placeholder(R.drawable.ic_profile_popcorn) // 로드 전
                        .into(binding.profileImg)
                    binding.image.clipToOutline = true
                } else {
                    binding.defaultImage.visibility = View.VISIBLE
                    Glide.with(binding.image).clear(binding.image)
                }

                binding.itemView.setOnClickListener { activity.addSearchLog(logList.get(position).memberId) }
            }
        }
    }
}