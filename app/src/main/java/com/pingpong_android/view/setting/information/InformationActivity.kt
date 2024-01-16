package com.pingpong_android.view.setting.information

import android.os.Bundle
import com.pingpong_android.BuildConfig
import com.pingpong_android.R
import com.pingpong_android.base.BaseActivity
import com.pingpong_android.databinding.ActivityInformationBinding

class InformationActivity : BaseActivity<ActivityInformationBinding>(R.layout.activity_information, TransitionMode.RIGHT) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = InformationViewModel()
        binding.activity = this

        initView()
    }

    private fun initView() {
        binding.appVersion.text = BuildConfig.VERSION_NAME
    }
}