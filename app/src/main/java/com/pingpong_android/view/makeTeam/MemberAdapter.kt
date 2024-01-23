package com.pingpong_android.view.makeTeam

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pingpong_android.databinding.ItemFriendListBinding
import com.pingpong_android.model.MemberDTO

class MemberAdapter(private var memberList: List<MemberDTO>) : RecyclerView.Adapter<MemberAdapter.MembersViewHolder>() {

    private lateinit var activity : MakeTeamActivity

    fun setActivity(activity: MakeTeamActivity) {
        this.activity = activity
    }

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

    fun getMemberId() : List<Long> {
        if (memberList.size > 1) {  // 유저 자신을 제외한 멤버가 있는지
            val memberIdList : MutableList<Long> = mutableListOf()

            // 자신 제외 멤버들의 id만
            for (memberDTO : MemberDTO in memberList.subList(1, memberList.size)) {
                memberIdList.add(memberDTO.memberId)
            }
            return memberIdList
        } else {
            return emptyList()
        }
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
                Glide.with(binding.profileImg)
                    .load(memberDTO.profileImage)
                    .into(binding.profileImg)
                binding.image.clipToOutline = true
            } else {
                binding.defaultImage.visibility = View.VISIBLE
                Glide.with(binding.image).clear(binding.image)
            }
        }
    }
}
