package com.pingpong_android.view.trash

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pingpong_android.databinding.ItemTodoInTrashBinding
import com.pingpong_android.model.MemberDTO
import com.pingpong_android.model.TrashDTO

class TrashAdapter : RecyclerView.Adapter<TrashAdapter.TrashViewHolder>() {

    private lateinit var activity : TrashActivity
    private var trashList : List<TrashDTO> = emptyList()

    fun setActivity(activity: TrashActivity) {
        this.activity = activity
    }

    fun addList(trashList: List<TrashDTO>) {
        this.trashList = trashList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int,
    ): TrashAdapter.TrashViewHolder {
        val binding = ItemTodoInTrashBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrashViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return trashList.size
    }

    override fun onBindViewHolder(holder: TrashViewHolder, position: Int) {
        holder.bind(trashList[position], position)
    }

    inner class TrashViewHolder(val binding : ItemTodoInTrashBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(trash : TrashDTO, position : Int) {
            profile(trash.manager)

            binding.title.text = trash.title
            binding.date.text = trash.date
            binding.btnEtc.setOnClickListener { activity.restoreTodo(trash) }
        }

        fun profile(user : MemberDTO?) {
            if (user == null) {
                binding.defaultImage.visibility = View.VISIBLE
                Glide.with(binding.profileImg).clear(binding.profileImg)
                binding.nickNm.text = ""
            } else {
                binding.nickNm.text = user.nickName

                if (user.profileImage.isNullOrEmpty()) {
                    binding.defaultImage.visibility = View.VISIBLE
                    Glide.with(binding.profileImg).clear(binding.profileImg)
                } else {
                    binding.defaultImage.visibility = View.GONE

                    Glide.with(binding.profileImg).load(user.profileImage)
                        .into(binding.profileImg)
                    binding.profileImg.clipToOutline = true
                }
            }
        }
    }
}