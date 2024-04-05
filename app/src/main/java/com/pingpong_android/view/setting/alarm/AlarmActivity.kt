package com.pingpong_android.view.setting.alarm

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = Manifest.permission.POST_NOTIFICATIONS
            if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
                binding.pushAlarm.isChecked = false
            } else {
                binding.pushAlarm.isChecked = true
            }
        } else {
            binding.pushAlarm.isChecked = true
        }
    }

    private fun setPermission() {
        // todo : alarm 설정 버튼 이벤트 처리
    }
}