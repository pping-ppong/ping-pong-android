package com.pingpong_android.view.setting.account

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.pingpong_android.R
import com.pingpong_android.base.BaseActivity
import com.pingpong_android.databinding.ActivityAccountBinding
import com.pingpong_android.view.gallery.GalleryActivity
import com.pingpong_android.view.login.LoginActivity

class AccountActivity : BaseActivity<ActivityAccountBinding>(R.layout.activity_account, TransitionMode.RIGHT) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = AccountViewModel()
        binding.activity = this

        subscribeDeleteAccount()
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
        binding.viewModel!!.requestDeleteAccount(prefs.getBearerToken())
    }

    private fun subscribeDeleteAccount() {
        binding.viewModel!!.result.observe(this, Observer {
            if (it.type == "SUCCESS_DELETE_MEMBER") {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                prefs.clearAll()
                goToLogin()
            } else {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun goToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }
}