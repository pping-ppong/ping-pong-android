package com.pingpong_android.view.intro

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.pingpong_android.R
import com.pingpong_android.base.BaseActivity
import com.pingpong_android.databinding.ActivityIntroBinding
import com.pingpong_android.model.OauthDTO
import com.pingpong_android.model.UserDTO
import com.pingpong_android.utils.PreferenceUtil
import com.pingpong_android.view.login.LoginActivity
import com.pingpong_android.view.main.MainActivity

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

        subscribeSocialInfo()
        subscribeLogin()
        subscribeReissue()

        reqeustSocialInfo()
    }

    private fun reqeustSocialInfo() {
        val oauthDTO = OauthDTO(userDTO.socialType, userDTO.code)
        binding.viewModel!!.requestSocialInfo(oauthDTO)
    }

    private fun subscribeSocialInfo() {
        binding.viewModel!!.userOauth.observe(this, Observer {
            if (it.socialId != null && it.email != null) {
                // 회원정보가 불러와진 경우
                // 로그인 요청
                userDTO.socialId = it.socialId
                userDTO.email = it.email
                binding.viewModel!!.requestLogin(userDTO)
            } else {
                // 회원정보가 없는 경우
                // 로그인 화면으로 이동
                goToLogin()
            }
        })
    }

    private fun subscribeLogin() {
        binding.viewModel!!.loginResult.observe(this, Observer {
            if (it.isSuccess) {
                // 로그인 요청 성공
                // 토근 재발행
                userDTO.memberId = it.userDTO.memberId
                userDTO.accessToken = it.userDTO.accessToken
                userDTO.refreshToken = it.userDTO.refreshToken
                binding.viewModel!!.requestReissue(userDTO)
            } else {
                // 로그인 요청 실패
                // 로그인 화면으로 이동
                goToLogin()
            }
        })
    }

    private fun subscribeReissue() {
        binding.viewModel!!.reissueResult.observe(this, Observer {
            if (it.isSuccess) {
                // 토큰 재발행 성공 시
                goToMain()
            } else {
                // 토큰 재발행 실패 시
                goToLogin()
            }
        })
    }

    fun goToMain() {
        prefs.saveUser(userDTO)

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }

    fun goToLogin() {
        prefs.saveUser(userDTO)

        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }
}