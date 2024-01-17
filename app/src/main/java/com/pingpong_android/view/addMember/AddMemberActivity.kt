package com.pingpong_android.view.addMember

import android.os.Bundle
import com.pingpong_android.R
import com.pingpong_android.base.BaseActivity
import com.pingpong_android.databinding.ActivityAddMemberBinding

class AddMemberActivity : BaseActivity<ActivityAddMemberBinding>(R.layout.activity_add_member) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = AddMemberViewModel()
        binding.activity = this

        initView()
    }

    private fun initView() {
        binding.topPanel.setLeftClickListener(listener = {onBackPressed()})
//        binding.topPanel.setRightClickListener(listener = {})

    }
}