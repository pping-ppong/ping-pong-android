package com.pingpong_android.view.gallery

import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.pingpong_android.R
import com.pingpong_android.base.BaseActivity
import com.pingpong_android.databinding.ActivityGalleryBinding

class GalleryActivity : BaseActivity<ActivityGalleryBinding>(R.layout.activity_gallery) {

    private var galleryAdapter = GalleryAdapter(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = GalleryViewModel()
        binding.activity = this

        initAdapter()

        binding.viewModel!!.fetchImageItemList(this)
    }

    private fun initAdapter() {
        val galleryLayoutManager = GridLayoutManager(this, 4)
        galleryAdapter.setActivity(this)

        binding.photoRv.apply {
            layoutManager = galleryLayoutManager
            adapter = galleryAdapter
        }

        binding.viewModel!!.imageItemList.observe(this, Observer {
            if (it.size > 0) {
                binding.noImage.visibility = View.GONE
                binding.photoRv.visibility = View.VISIBLE
                binding.btnDone.visibility = View.VISIBLE

                galleryAdapter.addList(it)
            } else {
                binding.noImage.visibility = View.VISIBLE
                binding.photoRv.visibility = View.GONE
                binding.btnDone.visibility = View.GONE

                galleryAdapter.addList(emptyList())
            }
        })
    }

    private fun setOnClick() {
        binding.btnDone.setOnClickListener { v -> {

        } }
    }

}