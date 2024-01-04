package com.pingpong_android.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pingpong_android.R
import com.pingpong_android.databinding.ItemFriendListBinding
import com.pingpong_android.model.UserDTO
import com.pingpong_android.view.friends.FriendActivity
import com.pingpong_android.view.search.SearchActivity

class FriendsAdapter(private var friendList : List<UserDTO>) : RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder>() {

    private lateinit var friendActivity: FriendActivity
    private lateinit var searchActivity: SearchActivity

    fun setFriendActivity(friendActivity: FriendActivity) {
        this.friendActivity = friendActivity
    }

    fun setSearchActivity(searchActivity: SearchActivity) {
        this.searchActivity = searchActivity
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FriendsAdapter.FriendsViewHolder {
        val binding = ItemFriendListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FriendsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FriendsAdapter.FriendsViewHolder, position: Int) {
        holder.bind(friendList.get(position), position)
    }

    override fun getItemCount(): Int {
        return friendList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addList(friends : List<UserDTO>) {
        friendList = friends
        notifyDataSetChanged()
    }

    inner class FriendsViewHolder(val binding : ItemFriendListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user : UserDTO, position : Int) {
            binding.nickNmEt.text = user.nickName
            Glide.with(binding.image).load(user.profileImage)
                .error(R.drawable.ic_profile_popcorn)   // 오류일 경우
                .fallback(R.drawable.ic_profile_popcorn)    // Null인 경우
                .placeholder(R.drawable.ic_profile_popcorn) // 로드 전
                .into(binding.image)
        }
    }
}