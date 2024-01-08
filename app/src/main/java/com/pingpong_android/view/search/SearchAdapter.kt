package com.pingpong_android.view.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pingpong_android.R
import com.pingpong_android.databinding.ItemFriendListBinding
import com.pingpong_android.model.MemberDTO

class SearchAdapter (private var friendList : List<MemberDTO>) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    private lateinit var searchActivity: SearchActivity

    fun setSearchActivity(searchActivity: SearchActivity) {
        this.searchActivity = searchActivity
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = ItemFriendListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(friendList.get(position), position)

        if (searchActivity != null) {
            holder.itemView.setOnClickListener { v -> searchActivity.addSearchLog(friendList.get(position).memberId) }
        }
    }

    override fun getItemCount(): Int {
        return friendList.size
    }

    fun addList(friends : List<MemberDTO>) {
        friendList = friends
        notifyDataSetChanged()
    }

    inner class SearchViewHolder(val binding : ItemFriendListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user : MemberDTO, position : Int) {
            binding.nickNmEt.text = user.nickName

            if (user.profileImage.isNullOrEmpty()) {
                binding.defaultImage.visibility = View.VISIBLE
            } else {
                binding.defaultImage.visibility = View.GONE

                Glide.with(binding.image).load(user.profileImage)
                    .error(R.drawable.ic_profile_popcorn)   // 오류일 경우
                    .fallback(R.drawable.ic_profile_popcorn)    // Null인 경우
                    .placeholder(R.drawable.ic_profile_popcorn) // 로드 전
                    .into(binding.image)
                binding.image.clipToOutline = true
            }
        }
    }
}