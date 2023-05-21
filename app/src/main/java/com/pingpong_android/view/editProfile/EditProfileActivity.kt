package com.pingpong_android.view.editProfile

import android.os.Bundle
import com.pingpong_android.R
import com.pingpong_android.base.BaseActivity
import com.pingpong_android.databinding.ActivityEditProfieBinding

class EditProfileActivity : BaseActivity<ActivityEditProfieBinding>(R.layout.activity_edit_profie) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = EditProfileViewModel()
        binding.activity = this
    }

}