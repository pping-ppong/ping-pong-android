package com.pingpong_android.view.friends

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pingpong_android.R
import com.pingpong_android.databinding.ItemFriendListBinding
import com.pingpong_android.model.MemberDTO
import com.pingpong_android.model.UserDTO

class FriendsAdapter(private var friendList : List<MemberDTO>) : RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder>() {

    private lateinit var friendActivity: FriendActivity

    fun setFriendActivity(friendActivity: FriendActivity) {
        this.friendActivity = friendActivity
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FriendsViewHolder {
        val binding = ItemFriendListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FriendsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FriendsViewHolder, position: Int) {
        holder.bind(friendList.get(position), position)

        if (friendActivity != null) {

        }
    }

    override fun getItemCount(): Int {
        return friendList.size
    }

    fun addList(friends : List<MemberDTO>) {
        friendList = friends
        notifyDataSetChanged()
    }

    inner class FriendsViewHolder(val binding : ItemFriendListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user : MemberDTO, position : Int) {
            binding.nickNmEt.text = user.nickName
            Glide.with(binding.image).load(user.profileImage)
                .error(R.drawable.ic_profile_popcorn)   // 오류일 경우
                .fallback(R.drawable.ic_profile_popcorn)    // Null인 경우
                .placeholder(R.drawable.ic_profile_popcorn) // 로드 전
                .into(binding.image)
        }
    }
}