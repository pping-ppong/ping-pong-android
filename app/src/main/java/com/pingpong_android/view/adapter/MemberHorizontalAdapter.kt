package com.pingpong_android.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pingpong_android.R
import com.pingpong_android.databinding.ItemImageWithTextBinding
import com.pingpong_android.model.MemberDTO

class MemberHorizontalAdapter(private var memberList: List<MemberDTO>, val forSelect : Boolean) : RecyclerView.Adapter<MemberHorizontalAdapter.MembersViewHolder>() {

    var selectedMemberDTO: MemberDTO? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MembersViewHolder {
        val binding = ItemImageWithTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        if (forSelect && selectedMemberDTO == null)
            selectedMemberDTO = memberList.get(0)
        return MembersViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return memberList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addList(memberList: List<MemberDTO>) {
        this.memberList = memberList
        notifyDataSetChanged()
    }

    fun getSelectedMember() : MemberDTO? {
        return selectedMemberDTO
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: MembersViewHolder, position: Int) {
        holder.bind(memberList.get(position), position)

        if (forSelect) {
            holder.itemView.setOnClickListener {
                selectedMemberDTO = memberList.get(position)
                notifyDataSetChanged()
            }
        }
    }

    inner class MembersViewHolder(val binding : ItemImageWithTextBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(memberDTO: MemberDTO, position : Int) {
            binding.title.text = memberDTO.nickName

            if (memberDTO.profileImage.isNotEmpty()) {
                binding.defaultImage.visibility = View.GONE
                Glide.with(binding.image)
                    .load(memberDTO.profileImage)
                    .into(binding.image)
            } else {
                binding.defaultImage.visibility = View.VISIBLE
                Glide.with(binding.image).clear(binding.image)
            }

            if (forSelect && memberDTO.memberId == selectedMemberDTO?.memberId) {
                binding.selectBorder.visibility = View.VISIBLE
            } else {
                binding.selectBorder.visibility = View.GONE
            }
        }
    }
}