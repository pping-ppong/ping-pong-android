package com.pingpong_android.view.main

import android.content.Intent
import android.os.Bundle
import com.pingpong_android.R
import com.pingpong_android.base.BaseActivity
import com.pingpong_android.base.Constants
import com.pingpong_android.databinding.ActivityMainBinding
import com.pingpong_android.model.UserDTO
import com.pingpong_android.utils.PreferenceUtil
import com.pingpong_android.view.join.JoinActivity
import com.pingpong_android.view.myPageActivity.MyPageActivity
import com.pingpong_android.view.notice.NoticeActivity
import com.pingpong_android.view.search.SearchActivity

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    companion object {
        lateinit var prefs: PreferenceUtil
        private lateinit var userDTO: UserDTO
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = MainViewModel()
        binding.activity = this
        prefs = PreferenceUtil(applicationContext)

        initUserDTO()
        setClickListener()
    }

    private fun initUserDTO() {
        prefs
        userDTO = prefs.getUser()
    }

    private fun setClickListener() {
        binding.btnMypage.setOnClickListener { goToMyPage() }
        binding.btnSearch.setOnClickListener { goToSearch()}
        binding.btnAlarm.setOnClickListener { goToNotice() }
    }

    private fun goToMyPage() {
        val intent = Intent(this, MyPageActivity::class.java)
        startActivity(intent)
    }

    private fun goToSearch() {
        val intent = Intent(this, SearchActivity::class.java)
        startActivity(intent)
    }

    private fun goToNotice() {
        val intent = Intent(this, NoticeActivity::class.java)
        startActivity(intent)
    }
}