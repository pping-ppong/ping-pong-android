package com.pingpong_android.view.groupCalendar

import android.os.Bundle
import com.pingpong_android.R
import com.pingpong_android.base.BaseActivity
import com.pingpong_android.databinding.ActivityGroupCalendarBinding

class GroupCalendarActivity : BaseActivity<ActivityGroupCalendarBinding>(R.layout.activity_group_calendar, TransitionMode.RIGHT) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = GroupCalendarViewModel()
        binding.activity = this

    }


}