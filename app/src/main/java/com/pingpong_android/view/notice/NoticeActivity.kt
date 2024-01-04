package com.pingpong_android.view.notice

import android.os.Bundle
import com.pingpong_android.R
import com.pingpong_android.base.BaseActivity
import com.pingpong_android.databinding.ActivityNoticeBinding


class NoticeActivity : BaseActivity<ActivityNoticeBinding>(R.layout.activity_notice) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = NoticeViewModel()
        binding.activity = this

        setClickListener()
    }

    private fun setClickListener() {

    }

}