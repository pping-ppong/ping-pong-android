package com.pingpong_android.view.editTeam

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pingpong_android.databinding.ItemFriendListBinding
import com.pingpong_android.model.MemberDTO
import com.pingpong_android.base.Status

class MemberAdapter(private var memberList: List<MemberDTO>, ) : RecyclerView.Adapter<MemberAdapter.MembersViewHolder>() {

    private var hostId : Long = 0
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

    @SuppressLint("NotifyDataSetChanged")
    fun addList(memberList: List<MemberDTO>) {
        this.memberList = memberList
        notifyDataSetChanged()
    }

    fun setHostId(hostId: Long) {
        this.hostId = hostId
    }

    inner class MembersViewHolder(val binding : ItemFriendListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(memberDTO: MemberDTO, position : Int) {
            // 주인장 아이콘
            if (memberDTO.memberId == hostId) {
                binding.icHost.visibility = View.VISIBLE
                binding.nickNmEt.text = memberDTO.nickName
            } else {
                binding.icHost.visibility = View.GONE
                binding.nickNmEt.text = memberDTO.nickName
            }

            // 유저 사진
            if (memberDTO.profileImage.isNotEmpty()) {
                binding.defaultImage.visibility = View.GONE
                Glide.with(binding.profileImg)
                    .load(memberDTO.profileImage)
                    .into(binding.profileImg)
                binding.image.clipToOutline = true
            } else {
                binding.defaultImage.visibility = View.VISIBLE
                Glide.with(binding.image).clear(binding.image)
            }

            // 새로 추가된 멤버 - new 표시
            if (memberDTO.status == Status.ACTIVE) {
                binding.originalMember.visibility = View.GONE
            } else {
                binding.originalMember.visibility = View.VISIBLE
            }
        }
    }
}
