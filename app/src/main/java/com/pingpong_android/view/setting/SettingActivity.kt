package com.pingpong_android.view.setting

import android.os.Bundle
import com.pingpong_android.R
import com.pingpong_android.base.BaseActivity
import com.pingpong_android.databinding.ActivitySettingBinding

class SettingActivity : BaseActivity<ActivitySettingBinding>(R.layout.activity_setting){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = SettingViewModel()
        binding.activity = this

    }


}