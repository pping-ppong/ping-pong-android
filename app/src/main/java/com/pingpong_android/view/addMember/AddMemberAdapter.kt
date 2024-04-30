package com.pingpong_android.view.addMember

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pingpong_android.databinding.ItemFriendWCheckBinding
import com.pingpong_android.model.MemberDTO

class AddMemberAdapter(private var friendList : List<MemberDTO>) : RecyclerView.Adapter<AddMemberAdapter.AddMemberViewHolder>() {

    private lateinit var activity: AddMemberActivity
    private var selectedList : MutableList<MemberDTO> = mutableListOf()
    private lateinit var memberList : List<MemberDTO> // 이미 그룹의 멤버 리스트
    private var fromEdit : Boolean = false

    fun setActivity(activity: AddMemberActivity) {
        this.activity = activity
    }

    fun addMemberList(memberList: List<MemberDTO>) { // 이미 그룹의 멤버 리스트
        this.memberList = memberList
        fromEdit = true
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): AddMemberViewHolder {
        val binding = ItemFriendWCheckBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddMemberViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddMemberViewHolder, position: Int) {
        holder.bind(friendList.get(position), position)
    }

    override fun getItemCount(): Int {
        return friendList.size
    }

    fun addList(friends : List<MemberDTO>) {
        friendList = friends
        notifyDataSetChanged()
    }

    fun getSelectedList() : List<MemberDTO> = selectedList

    inner class AddMemberViewHolder(val binding : ItemFriendWCheckBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(memberDTO: MemberDTO, position: Int) {
            binding.nickNmEt.text = memberDTO.nickName

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

            // 팀 수정에서 넘어온 경우,
            // 이미 팀에 속한 멤버일 때
            if (fromEdit && memberList.contains(memberDTO)) {
                binding.cover.visibility = View.VISIBLE
                // 클릭 이벤트 제거
                binding.btnCheck.visibility = View.GONE
                binding.btnCheck.setOnClickListener { null }
            } else {
                binding.cover.visibility = View.GONE
                // 클릭 이벤트 추가
                binding.btnCheck.visibility = View.VISIBLE
                binding.btnCheck.setOnClickListener {
                    if (!selectedList.contains(memberDTO)) //friendList[position]
                        selectedList.add(memberDTO) //friendList[position]
                }
            }
        }
    }
}