package com.pingpong_android.view.join

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.pingpong_android.R
import com.pingpong_android.base.BaseActivity
import com.pingpong_android.base.Constants.Companion.INTENT_EXTRA_USERDTO
import com.pingpong_android.databinding.ActivityJoinBinding
import com.pingpong_android.model.UserDTO
import com.pingpong_android.utils.PreferenceUtil
import com.pingpong_android.view.main.MainActivity

class JoinActivity : BaseActivity<ActivityJoinBinding>(R.layout.activity_join) {

    companion object {
        lateinit var prefs: PreferenceUtil
        private lateinit var userDTO: UserDTO
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = JoinViewModel()
        binding.activity = this

        prefs = PreferenceUtil(applicationContext)
        userDTO = intent.getSerializableExtra(INTENT_EXTRA_USERDTO) as UserDTO

        checkNickNameValidation()
        subscribeJoin()
        subscribeNickNmCheck()
        initView()
    }

    private fun initView() {
        if (userDTO.profileImage != null) {
            Glide.with(this).load(userDTO.profileImage).into(binding.profileImg)
            binding.defaultPhoto.visibility = View.INVISIBLE
        } else {
            binding.profileImg.visibility = View.INVISIBLE
            binding.defaultPhoto.visibility = View.INVISIBLE
        }
    }

    fun checkNickNameValidation() {
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

    private fun subscribeNickNmCheck() {
        binding.viewModel!!.nickNameCheckResult.observe(this, Observer {
            if (it.isSuccess) {
                if (it.code == 200) {
                    binding.nickNmEtLayout.error = null
                    userDTO.nickName = binding.nickNmEt.text.toString()
                    binding.viewModel!!.isReady.value = true
                }
            } else {
                if (it.message != null) {
                    binding.nickNmEtLayout.error = it.message
                    binding.viewModel!!.isReady.value = false
                } else
                    binding.viewModel!!.isReady.value = false
            }
        })
    }

    fun requestJoin() {
        binding.viewModel!!.requestJoin(userDTO)
    }

    private fun subscribeJoin() {
        binding.viewModel!!.userData.observe(this, Observer {
            if (it.isSuccess) {
                if (it.code == 200) {
                    userDTO.memberId = it.userDTO.memberId
                    userDTO.nickName = it.userDTO.nickName
                    userDTO.profileImage = it.userDTO.profileImage
                    goToMain(userDTO)
                } else
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            } else
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
        })
    }

    private fun subscribeReissue() {
        binding.viewModel!!.reissueResult.observe(this, Observer {
            if (it.isSuccess) {
                // 토큰 재발행 성공 시
            } else {
                // 토큰 재발행 실패 시
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun goToMain(userDTO: UserDTO) {
        prefs.saveUser(userDTO)
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }
}