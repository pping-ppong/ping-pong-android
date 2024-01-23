package com.pingpong_android.view.join

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.pingpong_android.R
import com.pingpong_android.base.BaseActivity
import com.pingpong_android.base.Constants.Companion.INTENT_EXTRA_USER_DTO
import com.pingpong_android.base.Constants.Companion.INTENT_EXTRA_WEB_URL
import com.pingpong_android.databinding.ActivityJoinBinding
import com.pingpong_android.model.UserDTO
import com.pingpong_android.view.main.MainActivity
import com.pingpong_android.view.webView.WebViewActivity

class JoinActivity : BaseActivity<ActivityJoinBinding>(R.layout.activity_join, TransitionMode.VERTICAL) {

    private lateinit var userDTO: UserDTO


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = JoinViewModel()
        binding.activity = this

        userDTO = intent.getSerializableExtra(INTENT_EXTRA_USER_DTO) as UserDTO

        checkNickNameValidation()
        checkTerms()

        initSubscribe()
        initView()
    }

    private fun initView() {
        if (userDTO.profileImage.isEmpty()) {
            Glide.with(this).load(userDTO.profileImage).into(binding.profileImg)
            binding.defaultPhoto.visibility = View.INVISIBLE
        } else {
            Glide.with(this).clear(binding.profileImg)
            binding.profileImg.visibility = View.INVISIBLE
            binding.defaultPhoto.visibility = View.INVISIBLE
        }
    }

    private fun checkNickNameValidation() {
        binding.nickNmEt.addTextChangedListener(textWatcher)
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            binding.viewModel!!.requestNickNameValidation(s.toString())
        }
    }

    private fun checkTerms() {
        binding.checkService.setOnCheckedChangeListener { button, isChecked ->
            binding.viewModel!!.isReadyTerms.value = isChecked && binding.checkUserInfo.isChecked
            checkAll()
        }

        binding.checkUserInfo.setOnCheckedChangeListener { button, isChecked ->
            binding.viewModel!!.isReadyTerms.value = isChecked && binding.checkService.isChecked
            checkAll()
        }
    }

    private fun checkAll() {
        binding.viewModel!!.isReadyAll.value = binding.viewModel!!.isReadyNickname.value!! && binding.viewModel!!.isReadyTerms.value!!
    }

    fun requestJoin() {
        binding.viewModel!!.requestJoin(userDTO)
    }

    private fun initSubscribe() {
        subscribeJoin()
        subscribeNickNmCheck()
        subscribeReissue()
    }

    private fun subscribeNickNmCheck() {
        binding.viewModel!!.nickNameCheckResult.observe(this, Observer {
            if (it.isSuccess) {
                if (it.code == 200) {
                    binding.nickNmEtLayout.error = null
                    userDTO.nickName = binding.nickNmEt.text.toString()
                    binding.viewModel!!.isReadyNickname.value = true
                    checkAll()
                }
            } else {
                if (it.message != null) {
                    binding.nickNmEtLayout.error = it.message
                    binding.viewModel!!.isReadyNickname.value = false
                } else
                    binding.viewModel!!.isReadyNickname.value = false
                checkAll()
            }
        })
    }

    private fun subscribeJoin() {
        binding.viewModel!!.userData.observe(this, Observer {
            if (it.isSuccess) {
                if (it.code == 200) {
                    userDTO.memberId = it.userDTO.memberId
                    userDTO.nickName = it.userDTO.nickName
                    userDTO.profileImage = it.userDTO.profileImage
                    binding.viewModel!!.requestLogin(userDTO)
                } else
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            } else
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
        })
    }

    private fun subscribeReissue() {
        binding.viewModel!!.userLogin.observe(this, Observer {
            if (it.isSuccess) {
                // 로그인 성공 시
                userDTO.accessToken = it.userDTO.accessToken
                userDTO.refreshToken = it.userDTO.refreshToken
                prefs.saveUser(userDTO)
                prefs.saveBearerToken(it.userDTO.accessToken)
                goToMain(userDTO)
            } else {
                // 로그인 실패 시
                Log.d("Join-Login", it.message)
                finish()
            }
        })
    }

    private fun goToMain(userDTO: UserDTO) {
        prefs.saveUser(userDTO)
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }

    fun goToWebView(type : Int) {
        var url = if (type == 1)
            getString(R.string.url_term_personal_information)
        else
            getString(R.string.url_term_service)

        val intent = Intent(this, WebViewActivity::class.java)
        intent.putExtra(INTENT_EXTRA_WEB_URL, url)
        startActivity(intent)
    }
}