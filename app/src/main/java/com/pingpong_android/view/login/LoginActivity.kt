package com.pingpong_android.view.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.pingpong_android.R
import com.pingpong_android.base.BaseActivity
import com.pingpong_android.base.Constants.Companion.INTENT_EXTRA_USERDTO
import com.pingpong_android.databinding.ActivityLoginBinding
import com.pingpong_android.model.OauthDTO
import com.pingpong_android.model.UserDTO
import com.pingpong_android.utils.PreferenceUtil
import com.pingpong_android.view.join.JoinActivity
import com.pingpong_android.view.main.MainActivity
import java.io.Serializable

class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {

    companion object {
        lateinit var prefs: PreferenceUtil
        private lateinit var userDTO: UserDTO
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = LoginViewModel()
        binding.activity = this

        prefs = PreferenceUtil(applicationContext)
        userDTO = prefs.getUser()

        subscribeLogin()
        subscribeUserInfo()
        setClickListener()

        if (!userDTO.socialId.isNullOrEmpty() && !userDTO.email.isNullOrEmpty())
            binding.viewModel!!.requestLogin(userDTO)
    }

    private fun setClickListener() {
        loginKakao()
        // 다른 SNS 로그인
    }

    private fun loginKakao() {
        binding.kakaoLogin.setOnClickListener{
            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if (error != null) {
                    Log.e("LOGIN", "카카오계정으로 로그인 실패", error)
                } else if (token != null) {
                    Log.i("LOGIN", "카카오계정으로 로그인 성공 ${token.accessToken}")
                }
            }

            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                    if (error != null) {
                        Toast.makeText(this@LoginActivity, "카카오 로그인 시도에 실패 했습니다.", Toast.LENGTH_SHORT).show()
                        Log.e("LOGIN", "카카오톡으로 로그인 실패", error)

                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }
                        UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                    } else if (token != null) {
                        UserApiClient.instance.me { user, error ->
                            userDTO = UserDTO(user!!.id.toString())
                            userDTO.email = user!!.kakaoAccount!!.email!!
                            userDTO.socialType = "KAKAO"
                            userDTO.socialToken = token.toString()
                            if (!user.kakaoAccount!!.profile!!.nickname.isNullOrEmpty())
                                userDTO.nickName = user.kakaoAccount!!.profile!!.nickname!!
                            if(!user.kakaoAccount!!.profile!!.profileImageUrl.isNullOrEmpty())
                                userDTO.profileImage = user.kakaoAccount!!.profile!!.profileImageUrl!!
                            prefs.saveUser(userDTO)

                            // 가입된 회원인지 확인
//                            val oauthDTO = OauthDTO("KAKAO", token)
                            binding.viewModel!!.requestSocialInfo(userDTO)
                        }
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
        }
    }

    fun subscribeUserInfo() {
        binding.viewModel!!.userOauth.observe(this, Observer {
            if (it.socialId != null) {
                binding.viewModel!!.requestLogin(it)
            } else {
                goToJoin()
            }
        })
    }

    fun subscribeLogin() {
        binding.viewModel!!.userData.observe(this, Observer {
            if (it.isSuccess) {

                goToMain()
            } else {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun goToJoin() {
        val intent = Intent(this, JoinActivity::class.java)
        intent.putExtra(INTENT_EXTRA_USERDTO, userDTO)
        startActivity(intent)
    }

    fun goToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }
}
