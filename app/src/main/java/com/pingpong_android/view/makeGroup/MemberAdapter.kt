package com.pingpong_android.view.makeGroup

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pingpong_android.R
import com.pingpong_android.databinding.ItemFriendListBinding
import com.pingpong_android.model.MemberDTO

class MemberAdapter(private var memberList: List<MemberDTO>) : RecyclerView.Adapter<MemberAdapter.MembersViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MembersViewHolder {
        var binding = ItemFriendListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MembersViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return memberList.size
    }

    override fun onBindViewHolder(holder: MembersViewHolder, position: Int) {
        holder.bind(memberList.get(position), position)
    }

    fun addList(memberList: List<MemberDTO>) {
        this.memberList = memberList
        notifyDataSetChanged()
    }

    inner class MembersViewHolder(val binding : ItemFriendListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(memberDTO: MemberDTO, position : Int) {
            // 주인장 아이콘
            if (position == 0) {
                binding.icHost.visibility = View.VISIBLE
                binding.nickNmEt.text = memberDTO.nickName + " (나)"
            } else {
                binding.icHost.visibility = View.GONE
                binding.nickNmEt.text = memberDTO.nickName
            }

            // 유저 사진
            if (memberDTO.profileImage.isNotEmpty()) {
                binding.defaultImage.visibility = View.GONE
                Glide.with(binding.profileImg).load(memberDTO.profileImage)
                    .error(R.drawable.ic_profile_popcorn)   // 오류일 경우
                    .fallback(R.drawable.ic_profile_popcorn)    // Null인 경우
                    .placeholder(R.drawable.ic_profile_popcorn) // 로드 전
                    .into(binding.profileImg)
                binding.image.clipToOutline = true
            } else {
                binding.defaultImage.visibility = View.VISIBLE
                Glide.with(binding.image).clear(binding.image)
            }
        }
    }
}
