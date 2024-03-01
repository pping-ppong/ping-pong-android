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

    fun setActivity(activity: AddMemberActivity) {
        this.activity = activity
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
            // 주인장 아이콘
            if (position == 0) {
                binding.icHost.visibility = View.VISIBLE
                binding.nickNmEt.text = memberDTO.nickName + " (나)"
                binding.btnCheck.visibility = View.GONE
            } else {
                binding.icHost.visibility = View.GONE
                binding.nickNmEt.text = memberDTO.nickName
                binding.btnCheck.visibility = View.VISIBLE
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

            // 클릭 이벤트
            if (position > 0) {
                binding.btnCheck.setOnClickListener {
                    if (!selectedList.contains(friendList[position]))
                        selectedList.add(friendList[position])
                }
            } else {
                binding.btnCheck.setOnClickListener(null)
            }

        }
    }
}