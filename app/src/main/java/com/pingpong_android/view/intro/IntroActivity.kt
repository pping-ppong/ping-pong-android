package com.pingpong_android.view.intro

import android.os.Bundle
import com.pingpong_android.R
import com.pingpong_android.base.BaseActivity
import com.pingpong_android.databinding.ActivityIntroBinding
import com.pingpong_android.model.OauthDTO
import com.pingpong_android.model.UserDTO
import com.pingpong_android.utils.PreferenceUtil
import com.pingpong_android.view.login.LoginActivity

class IntroActivity : BaseActivity<ActivityIntroBinding>(R.layout.activity_intro)  {

    companion object {
        lateinit var prefs: PreferenceUtil
        private lateinit var userDTO: UserDTO
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = IntroViewModel()
        binding.activity = this

        prefs = PreferenceUtil(applicationContext)
        userDTO = prefs.getUser()

        reqeustSocialInfo()
    }

    private fun reqeustSocialInfo() {
        binding.viewModel.requestSocialInfo(userDTO)
    }
}