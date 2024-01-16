package com.pingpong_android.view.setting

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.pingpong_android.R
import com.pingpong_android.base.BaseActivity
import com.pingpong_android.base.Constants
import com.pingpong_android.databinding.ActivitySettingBinding
import com.pingpong_android.view.intro.IntroActivity
import com.pingpong_android.view.login.LoginActivity
import com.pingpong_android.view.profile.ProfileActivity
import com.pingpong_android.view.setting.account.AccountActivity
import com.pingpong_android.view.setting.alarm.AlarmActivity
import com.pingpong_android.view.setting.announcement.AnnouncementActivity
import com.pingpong_android.view.setting.information.InformationActivity

class SettingActivity : BaseActivity<ActivitySettingBinding>(R.layout.activity_setting, TransitionMode.RIGHT){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = SettingViewModel()
        binding.activity = this

        subscribeLogout()
    }

    private fun subscribeLogout() {
        binding.viewModel!!.loginResult.observe(this, Observer {
            if (it.isSuccess) {
                prefs.clearAll()
                goToLogin()
            } else {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    fun requestLogout() {
        binding.viewModel!!.requestLogout(prefs.getUser())
    }

    fun goToAccount() {
        val intent = Intent(this, AccountActivity::class.java)
        startActivity(intent)
    }

    fun goToAlarm() {
        val intent = Intent(this, AlarmActivity::class.java)
        startActivity(intent)
    }

    fun goToAnnouncement() {
        val intent = Intent(this, AnnouncementActivity::class.java)
        startActivity(intent)
    }

    fun goToInformation() {
        val intent = Intent(this, InformationActivity::class.java)
        startActivity(intent)
    }

    private fun goToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        finishAffinity()
        startActivity(intent)
    }
}