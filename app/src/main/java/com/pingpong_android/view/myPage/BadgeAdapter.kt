package com.pingpong_android.view.myPage

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pingpong_android.databinding.ItemBadgeBinding
import com.pingpong_android.model.BadgeDTO

class BadgeAdapter(private var badgeList: List<BadgeDTO>) : RecyclerView.Adapter<BadgeAdapter.BadgeViewHolder>() {

    private lateinit var context: Context

    fun setContext(context: Context) {
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BadgeViewHolder {
        val binding = ItemBadgeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BadgeViewHolder(binding)
    }

    override fun onBindViewHolder(holder : BadgeViewHolder, position: Int) {
        holder.bind(badgeList[position], position)
    }

    override fun getItemCount(): Int {
        return badgeList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addList(badgeList: List<BadgeDTO>) {
        this.badgeList = badgeList
        notifyDataSetChanged()
    }


    inner class BadgeViewHolder(val binding : ItemBadgeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(badge : BadgeDTO, position : Int) {
            binding.badgeTv.text = badge.badgeName
        }
    }
}