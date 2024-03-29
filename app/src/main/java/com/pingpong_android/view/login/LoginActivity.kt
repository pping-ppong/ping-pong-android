package com.pingpong_android.view.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.User
import com.pingpong_android.BuildConfig
import com.pingpong_android.R
import com.pingpong_android.base.BaseActivity
import com.pingpong_android.base.Constants.Companion.INTENT_EXTRA_USER_DTO
import com.pingpong_android.databinding.ActivityLoginBinding
import com.pingpong_android.model.OauthDTO
import com.pingpong_android.model.UserDTO
import com.pingpong_android.utils.PreferenceUtil
import com.pingpong_android.view.join.JoinActivity
import com.pingpong_android.view.main.MainActivity

class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {

    private lateinit var userDTO: UserDTO
    private val googleSignInClient: GoogleSignInClient by lazy { getGoogleClient() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = LoginViewModel()
        binding.activity = this

        userDTO = prefs.getUser()

        initSubscribe()
        setClickListener()
    }

    private fun setClickListener() {
        loginKakao()
        loginGoogle()
    }

    private fun initSubscribe() {
        subscribeLogin()
    }

    private fun loginKakao() {
        // 카카오 로그인 및 회원가입 요청
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
                        Log.e("HTTP-LOGIN", "카카오톡으로 로그인 실패", error)

                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }
                        UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                    } else if (token != null) {
                        // 카카오 로그인 정보 요청 성공
                        UserApiClient.instance.me { user, error ->
                            userDTO = UserDTO(user!!.id.toString()) // socialId
                            userDTO.email = user!!.kakaoAccount!!.email!!
                            userDTO.socialType = "KAKAO"
                            userDTO.code = token.accessToken
                            userDTO.accessToken = token.accessToken
                            userDTO.refreshToken = token.refreshToken
                            if (!user.kakaoAccount!!.profile!!.nickname.isNullOrEmpty())
                                userDTO.nickName = user.kakaoAccount!!.profile!!.nickname!!
                            if(!user.kakaoAccount!!.profile!!.profileImageUrl.isNullOrEmpty())
                                userDTO.profileImage = user.kakaoAccount!!.profile!!.profileImageUrl!!
                            prefs.saveUser(userDTO)

                            // 가입된 회원인지 확인
                            binding.viewModel!!.requestLogin(userDTO)
                        }
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
        }
    }

    private fun loginGoogle() {
        binding.googleLogin.setOnClickListener {
            googleSignInClient.signOut()
            val signInIntent = googleSignInClient.signInIntent
            googleAuthLauncher.launch(signInIntent)
        }
    }

    private val googleAuthLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

            try {
                val account = task.getResult(ApiException::class.java)

                // 이름, 이메일 등이 필요하다면 아래와 같이 account를 통해 각 메소드를 불러올 수 있다.
                userDTO = UserDTO(account.id.toString()) // socialId
                userDTO.email = account.email.toString()
                userDTO.socialType = "GOOGLE"
                userDTO.nickName = account.givenName!!
                prefs.saveUser(userDTO)

                // 가입된 회원인지 확인
                binding.viewModel!!.requestLogin(userDTO)
            } catch (e: ApiException) {
                Log.e(LoginActivity::class.java.simpleName, e.stackTraceToString())
                Toast.makeText(this, e.statusCode.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getGoogleClient(): GoogleSignInClient {
        val googleSignInOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(Scope("https://www.googleapis.com/auth/pubsub"))
            .requestServerAuthCode(BuildConfig.GOOGLE_REQUEST_ID_TOKEN) // string 파일에 저장해둔 client id 를 이용해 server authcode를 요청한다.
            .requestEmail() // 이메일도 요청할 수 있다.
            .build()

        return GoogleSignIn.getClient(this, googleSignInOption)
    }

    private fun subscribeLogin() {
        binding.viewModel!!.userData.observe(this, Observer {
            if (it.isSuccess && it.userDTO != null) {
                userDTO.memberId = it.userDTO.memberId
                userDTO.accessToken = it.userDTO.accessToken
                userDTO.refreshToken = it.userDTO.refreshToken
                prefs.saveBearerToken(it.userDTO.accessToken)
                goToMain()
            } else {
                goToJoin()
            }
        })
    }

    private fun goToJoin() {
        prefs.saveUser(userDTO)
        val intent = Intent(this, JoinActivity::class.java)
        intent.putExtra(INTENT_EXTRA_USER_DTO, userDTO)
        startActivity(intent)
    }

    private fun goToMain() {
        prefs.saveUser(userDTO)
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }
}