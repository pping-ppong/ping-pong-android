package com.pingpong_android.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pingpong_android.R
import com.pingpong_android.databinding.ItemImageWithTextBinding
import com.pingpong_android.model.MemberDTO

class MembersAdapter(private val memberList: List<MemberDTO>) : RecyclerView.Adapter<MembersAdapter.MembersViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MembersViewHolder {
        var binding = ItemImageWithTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MembersViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return memberList.size
    }

    override fun onBindViewHolder(holder: MembersViewHolder, position: Int) {
        holder.bind(memberList.get(position), position)
    }

    inner class MembersViewHolder(val binding : ItemImageWithTextBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(memberDTO: MemberDTO, position : Int) {
            binding.title.text = memberDTO.nickname
            Glide.with(binding.image).load(memberDTO.profileImage)
                .error(R.drawable.ic_profile_popcorn)   // 오류일 경우
                .fallback(R.drawable.ic_profile_popcorn)    // Null인 경우
                .placeholder(R.drawable.ic_profile_popcorn) // 로드 전
                .into(binding.image)
        }
    }
}