package com.pingpong_android.view.setting.alarm

import android.os.Bundle
import com.pingpong_android.R
import com.pingpong_android.base.BaseActivity
import com.pingpong_android.databinding.ActivityAlarmBinding

class AlarmActivity : BaseActivity<ActivityAlarmBinding>(R.layout.activity_alarm, TransitionMode.RIGHT) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = AlarmViewModel()
        binding.activity = this

        initAlarm()
    }

    private fun initAlarm() {
        binding.topPanel.setLeftClickListener(listener = {onBackPressed()})
        // todo : alarm 초기 세팅
    }
}