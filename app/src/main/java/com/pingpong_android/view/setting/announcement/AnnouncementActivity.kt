package com.pingpong_android.view.setting.announcement

import android.os.Bundle
import com.pingpong_android.R
import com.pingpong_android.base.BaseActivity
import com.pingpong_android.databinding.ActivityAnnouncementBinding

class AnnouncementActivity : BaseActivity<ActivityAnnouncementBinding>(R.layout.activity_announcement, TransitionMode.RIGHT) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = AnnouncementViewModel()
        binding.activity = this

        // todo : 공지사항 api 필요
    }
}