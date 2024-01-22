package com.pingpong_android.view.setting.account

import android.os.Bundle
import com.bumptech.glide.Glide
import com.pingpong_android.R
import com.pingpong_android.base.BaseActivity
import com.pingpong_android.databinding.ActivityAccountBinding

class AccountActivity : BaseActivity<ActivityAccountBinding>(R.layout.activity_account, TransitionMode.RIGHT) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = AccountViewModel()
        binding.activity = this

        initView()
    }

    private fun initView() {
        binding.topPanel.setLeftClickListener(listener = { onBackPressed() })
        val user = prefs.getUser()

        binding.emailTxt.text = user.email
        if (user.socialType == "GOOGLE")
            Glide.with(binding.socialIcon).load(R.drawable.ic_google_logo).into(binding.socialIcon)
        else if (user.socialType == "KAKAO")
            Glide.with(binding.socialIcon).load(R.drawable.ic_kakao_logo).into(binding.socialIcon)
    }

    fun requestDeleteAccount() {
        binding.viewModel!!.requestLogout(prefs.getBearerToken(), prefs.getId())
        prefs.clearAll()
    }
}