package com.pingpong_android.view.gallery

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pingpong_android.R
import com.pingpong_android.databinding.ItemPhotoBinding
import com.pingpong_android.model.ImageItem

class GalleryAdapter(private var photoList: List<ImageItem>) : RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {

    private lateinit var activity: GalleryActivity
    private var selected = -1

    fun setActivity(activity: GalleryActivity) {
        this.activity = activity
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GalleryAdapter.GalleryViewHolder {
        val binding = ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GalleryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GalleryAdapter.GalleryViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.bind(photoList.get(position), position)

        holder.itemView.setOnClickListener { v ->
            run {
                activity.binding.viewModel!!.isReady.postValue(true)
                selected = position
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

    fun addList(photoList: List<ImageItem>) {
        this.photoList = photoList
        notifyDataSetChanged()
    }

    fun getSelectedPhoto() : ImageItem {
        return if (selected == -1) ImageItem(null, false) else photoList.get(selected)
    }

    inner class GalleryViewHolder(val binding : ItemPhotoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(photo : ImageItem, position: Int) {
            Glide.with(binding.image).load(photo.uri)
                .error(R.drawable.ic_profile_popcorn)   // 오류일 경우
                .fallback(R.drawable.ic_profile_popcorn)    // Null인 경우
                .placeholder(R.drawable.ic_profile_popcorn) // 로드 전
                .into(binding.image)

            if (selected == position)
                binding.checked.visibility = View.VISIBLE
            else
                binding.checked.visibility = View.GONE
        }
    }
}