package com.pingpong_android.view.friendList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pingpong_android.databinding.ItemFriendListBinding
import com.pingpong_android.model.MemberDTO

class FriendsAdapter(private var friendList : List<MemberDTO>) : RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder>() {

    private lateinit var activity: FriendActivity

    fun setFriendActivity(activity: FriendActivity) {
        this.activity = activity
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

        if (activity != null) {
            holder.itemView.setOnClickListener { activity.goToUserProfile(friendList.get(position).memberId) }
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
            // 주인장 아이콘
            if (position == 0) {
                binding.icHost.visibility = View.VISIBLE
                binding.nickNmEt.text = user.nickName + " (나)"
            } else {
                binding.icHost.visibility = View.GONE
                binding.nickNmEt.text = user.nickName
            }

            // 유저 사진
            if (user.profileImage.isNotEmpty()) {
                binding.defaultImage.visibility = View.GONE
                Glide.with(binding.profileImg)
                    .load(user.profileImage)
                    .into(binding.profileImg)
                binding.image.clipToOutline = true
            } else {
                binding.defaultImage.visibility = View.VISIBLE
                Glide.with(binding.image).clear(binding.image)
            }
        }
    }
}