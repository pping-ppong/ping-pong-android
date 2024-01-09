package com.pingpong_android.view.notice

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pingpong_android.R
import com.pingpong_android.base.Constants.Companion.FRIEND
import com.pingpong_android.base.Constants.Companion.TODO
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

    override fun onBindViewHolder(holder: NoticeAdapter.NoticeViewHolder, position: Int) {
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
            if (notice.type == FRIEND) {
                // 친구 관련 알림
                binding.btnConfirm.visibility = View.VISIBLE
                binding.btnConfirm.visibility = View.VISIBLE

                // 메세지
                binding.message.text = notice.message

                // 사진 설정
                if (notice.profileImage.isNullOrEmpty()) {
                    binding.defaultImage.visibility = View.VISIBLE
                    Glide.with(binding.image).clear(binding.image)
                } else {
                    binding.defaultImage.visibility = View.GONE

                    Glide.with(binding.image).load(notice.profileImage)
                        .error(R.drawable.ic_profile_popcorn)   // 오류일 경우
                        .fallback(R.drawable.ic_profile_popcorn)    // Null인 경우
                        .placeholder(R.drawable.ic_profile_popcorn) // 로드 전
                        .into(binding.image)
                }

                // todo : 버튼 클릭 이벤트
            } else if (notice.type == TODO) {
                // 할일 관련 알림
                binding.btnConfirm.visibility = View.GONE
                binding.btnConfirm.visibility = View.GONE

                // 메세지
                binding.message.text = notice.message
            }
        }
    }
}