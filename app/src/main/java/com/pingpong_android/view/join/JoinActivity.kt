package com.pingpong_android.view.join

import android.content.Intent
import android.os.Bundle
import com.pingpong_android.R
import com.pingpong_android.base.BaseActivity
import com.pingpong_android.databinding.ActivityJoinBinding
import com.pingpong_android.view.main.MainActivity

class JoinActivity : BaseActivity<ActivityJoinBinding>(R.layout.activity_join) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = JoinViewModel()
        binding.activity = this

        setClickListener()
    }

    private fun setClickListener() {
        binding.btnDone.setOnClickListener { goToMain() }

    }

    private fun goToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}