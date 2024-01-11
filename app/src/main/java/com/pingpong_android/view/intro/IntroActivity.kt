package com.pingpong_android.view.intro

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.kakao.sdk.common.util.Utility
import com.pingpong_android.R
import com.pingpong_android.base.BaseActivity
import com.pingpong_android.databinding.ActivityIntroBinding
import com.pingpong_android.model.OauthDTO
import com.pingpong_android.model.UserDTO
import com.pingpong_android.utils.PreferenceUtil
import com.pingpong_android.view.login.LoginActivity
import com.pingpong_android.view.main.MainActivity

class IntroActivity : BaseActivity<ActivityIntroBinding>(R.layout.activity_intro)  {

    private val REQUEST_PERMISSIONS = 1

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


        /* temp */
//        userDTO.socialId = "3277875718"
//        userDTO.email = "minji.7754.7754@kakao.com"

        subscribeLogin()
        subscribeReissue()

        checkPermission()
    }

    private fun checkPermission() {
        var permission = mutableMapOf<String, String>()

        permission["storageWrite"] =  Manifest.permission.WRITE_EXTERNAL_STORAGE
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            permission["storageRead"] = Manifest.permission.READ_MEDIA_IMAGES
        else
            permission["storageRead"] = Manifest.permission.READ_EXTERNAL_STORAGE

        // 현재 권한 상태 검사
        var denied = permission.count { ContextCompat.checkSelfPermission(this, it.value)  == PackageManager.PERMISSION_DENIED }

        if(denied > 0)
            requestPermissions(permission.values.toTypedArray(), REQUEST_PERMISSIONS)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == REQUEST_PERMISSIONS) {

            /* 1. 권한 확인이 다 끝난 후 동의하지 않은 것이 있다면 종료 */
            var count = grantResults.count { it == PackageManager.PERMISSION_DENIED } // 동의 안한 권한의 개수
            if(count != 0) {
                requestSocialInfo()
            } else {
                Toast.makeText(applicationContext, "권한 미동의로 어플 이용이 어려운 서비스가 있을 수 있습니다.", Toast.LENGTH_SHORT).show()
            }


            /* 2. 권한 요청을 거부했다면 안내 메시지 보여주며 앱 종료
            grantResults.forEach {
                if(it == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(applicationContext, "권한 미동의로 어플 이용이 어려운 서비스가 있을 수 있습니다.", Toast.LENGTH_SHORT).show()
                }
                reqeustSocialInfo()
            }*/
        }
    }

    private fun requestSocialInfo() {
        if (userDTO != null)
            binding.viewModel!!.requestLogin(userDTO)
    }

    private fun subscribeLogin() {
        binding.viewModel!!.loginResult.observe(this, Observer {
            if (it.isSuccess) {
                // 로그인 요청 성공
                // 토근 재발행
                userDTO.memberId = it.userDTO.memberId
                userDTO.accessToken = it.userDTO.accessToken
                userDTO.refreshToken = it.userDTO.refreshToken
                prefs.saveBearerToken(it.userDTO.accessToken)
                goToMain()
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
                userDTO.accessToken = it.userDTO.accessToken
                userDTO.refreshToken = it.userDTO.refreshToken
                prefs.saveBearerToken(it.userDTO.accessToken)
                goToMain()
            } else {
                // 토큰 재발행 실패 시
                goToLogin()
            }
        })
    }

    private fun goToMain() {
        prefs.saveUser(userDTO)

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }

    private fun goToLogin() {
        prefs.saveUser(userDTO)

        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }
}