package com.pingpong_android.view.notice

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pingpong_android.R
import com.pingpong_android.base.Constants.Companion.FRIEND
import com.pingpong_android.base.Constants.Companion.TEAM
import com.pingpong_android.base.Constants.Companion.TO_DO
import com.pingpong_android.databinding.ItemNoticeBinding
import com.pingpong_android.model.NoticeDTO

class NoticeAdapter(private var noticeList: List<NoticeDTO>) : RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder>() {

    private lateinit var activity: NoticeActivity

    fun setActivity(activity: NoticeActivity) {
        this.activity = activity
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NoticeAdapter.NoticeViewHolder {
        val binding = ItemNoticeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoticeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoticeViewHolder, position: Int) {
        holder.bind(noticeList.get(position), position)
    }

    override fun getItemCount(): Int {
        return noticeList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addList(noticeList: List<NoticeDTO>) {
        this.noticeList = noticeList
        notifyDataSetChanged()
    }

    inner class NoticeViewHolder(val binding : ItemNoticeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(notice : NoticeDTO, position: Int) {
            binding.date.text = notice.createdAt

            if (notice.type == FRIEND) {
                if (!notice.isAccepted) {   // 수락 또는 거절 요청 전
                    // 친구 관련 알림
                    binding.btnConfirm.visibility = View.VISIBLE
                    binding.btnReject.visibility = View.VISIBLE

                    // 메세지
                    binding.message.text = notice.message

                    // 사진 설정
                    if (notice.profileImage.isNullOrEmpty()) {
                        binding.defaultImage.visibility = View.VISIBLE
                        Glide.with(binding.image).clear(binding.image)
                    } else {
                        binding.defaultImage.visibility = View.GONE

                        Glide.with(binding.image).load(notice.profileImage)
                            .into(binding.image)
                    }

                    binding.btnConfirm.setOnClickListener { activity.acceptFriendShip(notice.memberId) }
                    binding.btnReject.setOnClickListener { activity.refuseFriendShip(notice.memberId) }
                } else {
                    // 친구 관련 알림
                    binding.btnConfirm.visibility = View.GONE
                    binding.btnReject.visibility = View.GONE

                    // 메세지
                    binding.message.text = notice.message

                    // 사진 설정
                    if (notice.profileImage.isNullOrEmpty()) {
                        binding.defaultImage.visibility = View.VISIBLE
                        Glide.with(binding.image).clear(binding.image)
                    } else {
                        binding.defaultImage.visibility = View.GONE

                        Glide.with(binding.image).load(notice.profileImage)
                            .into(binding.image)
                    }
                }
            } else if (notice.type == TO_DO) {
                // 할일 관련 알림
                binding.btnConfirm.visibility = View.GONE
                binding.btnReject.visibility = View.GONE

                // 메세지
                binding.message.text = notice.message

                // 사진 설정
                if (notice.profileImage.isNullOrEmpty()) {
                    binding.defaultImage.visibility = View.VISIBLE
                    Glide.with(binding.image).clear(binding.image)
                } else {
                    binding.defaultImage.visibility = View.GONE

                    Glide.with(binding.image).load(notice.profileImage)
                        .into(binding.image)
                }
            } else if (notice.type == TEAM) {
                if (!notice.isAccepted) {   // 수락 또는 거절 요청 전
                    // 팀 관련 알림
                    binding.btnConfirm.visibility = View.VISIBLE
                    binding.btnReject.visibility = View.VISIBLE

                    // 메세지
                    binding.message.text = notice.message

                    // 사진 설정
                    if (notice.profileImage.isNullOrEmpty()) {
                        binding.defaultImage.visibility = View.VISIBLE
                        Glide.with(binding.image).clear(binding.image)
                    } else {
                        binding.defaultImage.visibility = View.GONE

                        Glide.with(binding.image).load(notice.profileImage)
                            .into(binding.image)
                    }

                    binding.btnConfirm.setOnClickListener { activity.acceptTeamInvite(notice) }
                    binding.btnReject.setOnClickListener { activity.refuseTeamInvite(notice) }
                } else {
                    // 팀 관련 알림
                    binding.btnConfirm.visibility = View.GONE
                    binding.btnReject.visibility = View.GONE

                    // 메세지
                    binding.message.text = notice.message

                    // 사진 설정
                    if (notice.profileImage.isNullOrEmpty()) {
                        binding.defaultImage.visibility = View.VISIBLE
                        Glide.with(binding.image).clear(binding.image)
                    } else {
                        binding.defaultImage.visibility = View.GONE

                        Glide.with(binding.image).load(notice.profileImage)
                            .into(binding.image)
                    }
                }
            }
        }
    }
}