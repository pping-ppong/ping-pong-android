package com.pingpong_android.view.gallery

import android.content.ContentUris
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.pingpong_android.R
import com.pingpong_android.base.BaseActivity
import com.pingpong_android.base.Constants.Companion.INTENT_EXTRA_URI
import com.pingpong_android.databinding.ActivityGalleryBinding
import com.pingpong_android.view.editProfile.EditProfileActivity
import com.pingpong_android.view.join.JoinActivity

class GalleryActivity : BaseActivity<ActivityGalleryBinding>(R.layout.activity_gallery, TransitionMode.RIGHT) {

    private var galleryAdapter = GalleryAdapter(emptyList())
    private var FROM_JOIN : Boolean = false
    private var FROM_EDIT : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = GalleryViewModel()
        binding.activity = this

        initData()
        initAdapter()

        binding.topPanel.setLeftClickListener(listener = {onBackPressed()})
        binding.viewModel!!.fetchImageItemList(this)
    }

    private fun initData() {
        FROM_JOIN = intent.getBooleanExtra("FROM_JOIN", false)
        FROM_EDIT = intent.getBooleanExtra("FROM_EDIT", false)
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

    fun onApplyClick() {
        if (galleryAdapter.getSelectedPhoto().uri != null) {
            val intent : Intent
            if (FROM_JOIN)
                intent = Intent(this, JoinActivity::class.java)
            else if (FROM_EDIT)
                intent = Intent(this, EditProfileActivity::class.java)
            else
                intent = Intent(this, EditProfileActivity::class.java)

            intent.putExtra(INTENT_EXTRA_URI, galleryAdapter.getSelectedPhoto().uri.toString())
            setResult(RESULT_OK, intent)
        }
    }
}