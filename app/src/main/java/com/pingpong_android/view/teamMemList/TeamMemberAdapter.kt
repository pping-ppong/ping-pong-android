package com.pingpong_android.view.teamMemList

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pingpong_android.R
import com.pingpong_android.base.Status
import com.pingpong_android.databinding.ItemFriendListBinding
import com.pingpong_android.model.MemberDTO

class TeamMemberAdapter(private var memberList : List<MemberDTO>) : RecyclerView.Adapter<TeamMemberAdapter.TeamMemberViewHolder>() {

    private lateinit var activity: TeamMemberActivity
    private var isHost : Boolean = false

    fun setTeamMemberActivity(activity: TeamMemberActivity) {
        this.activity = activity
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): TeamMemberViewHolder {
        val binding = ItemFriendListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TeamMemberViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TeamMemberViewHolder, position: Int) {
        holder.bind(memberList[position], position)
    }

    override fun getItemCount(): Int {
        return memberList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addList(members : List<MemberDTO>) {
        memberList = members
        isHost = members[0].hostId == activity.myId
        notifyDataSetChanged()
    }

    inner class TeamMemberViewHolder(val binding : ItemFriendListBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(user : MemberDTO, position : Int) {
            profileImg(user)

            if (user.memberId == user.hostId)
                binding.icHost.visibility = View.VISIBLE
            else
                binding.icHost.visibility = View.GONE

            // me & others
            if (user.memberId == activity.myId) {  // me
                binding.nickNmEt.text = user.nickName + " (나)"
                binding.itemView.setOnClickListener { null }
                binding.btnFriend.visibility = View.GONE
            } else {                                         // others
                binding.nickNmEt.text = user.nickName
                binding.itemView.setOnClickListener { activity.goToUserProfile(user.memberId) }
                binding.btnFriend.visibility = View.VISIBLE

                // 버튼 상태
                when (user.friendStatus) {
                    Status.ACTIVE -> {  // 친구 상태
                        binding.btnFriend.text = activity.getString(R.string.friend)
                        binding.btnFriend.setTextColor(activity.getColor(R.color.black))
                        binding.btnFriend.background = activity.getDrawable(R.drawable.back_white_stroke_gray_30dp)
                        binding.btnFriend.setOnClickListener { null }
                    }
                    Status.WAIT -> {    // 친구 수락 전
                        binding.btnFriend.text = activity.getString(R.string.invite_complete)
                        binding.btnFriend.setTextColor(activity.getColor(R.color.black))
                        binding.btnFriend.background =
                            activity.getDrawable(R.drawable.back_white_stroke_gray_30dp)
                        binding.btnFriend.setOnClickListener { null }
                    }
                    Status.INACTIVE -> {// 아무 관계 없음
                        binding.btnFriend.text = activity.getString(R.string.add_friend)
                        binding.btnFriend.setTextColor(activity.getColor(R.color.white))
                        binding.btnFriend.background = activity.getDrawable(R.drawable.back_black_30dp)
                        binding.btnFriend.setOnClickListener { v -> activity.requestFriendship(user.memberId) }
                    }
                    Status.DELETE -> {  // 거절
                        binding.btnFriend.text = activity.getString(R.string.add_friend)
                        binding.btnFriend.setTextColor(activity.getColor(R.color.white))
                        binding.btnFriend.background = activity.getDrawable(R.drawable.back_black_30dp)
                        binding.btnFriend.setOnClickListener { v -> activity.requestFriendship(user.memberId) }
                    }
                }

                // 호스트인 경우
                if (isHost) {
                    binding.itemView.setOnLongClickListener {
                        activity.onClickHostDialog(user)
                        return@setOnLongClickListener true
                    }
                } else {
                    binding.itemView.setOnLongClickListener {
                        return@setOnLongClickListener false
                    }
                }
            }
        }

        fun profileImg(user : MemberDTO) {
            if (user.profileImage.isNullOrEmpty()) {
                binding.defaultImage.visibility = View.VISIBLE
            } else {
                binding.defaultImage.visibility = View.GONE

                Glide.with(binding.profileImg).load(user.profileImage)
                    .into(binding.profileImg)
                binding.image.clipToOutline = true
            }
        }
    }
}