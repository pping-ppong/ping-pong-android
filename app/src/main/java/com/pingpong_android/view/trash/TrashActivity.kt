package com.pingpong_android.view.trash

import android.os.Bundle
import com.pingpong_android.R
import com.pingpong_android.base.BaseActivity
import com.pingpong_android.databinding.ActivityTrashBinding

class TrashActivity : BaseActivity<ActivityTrashBinding>(R.layout.activity_trash) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = TrashViewModel()
        binding.activity = this

    }
}