package com.pingpong_android.view.myPageActivity

import android.content.Intent
import android.os.Bundle
import com.pingpong_android.R
import com.pingpong_android.base.BaseActivity
import com.pingpong_android.databinding.ActivityMyPageBinding
import com.pingpong_android.view.editProfile.EditProfileActivity
import com.pingpong_android.view.friends.FriendActivity

class MyPageActivity : BaseActivity<ActivityMyPageBinding>(R.layout.activity_my_page) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = MyPageViewModel()
        binding.activity = this

        setClickListener()
    }

    private fun setClickListener() {
        binding.btnProfile.setOnClickListener { goToMain() }
        binding.btnFriend.setOnClickListener { goToFriend() }
    }

    private fun goToMain() {
        val intent = Intent(this, EditProfileActivity::class.java)
        startActivity(intent)
    }

    private fun goToFriend() {
        val intent = Intent(this, FriendActivity::class.java)
        startActivity(intent)
    }
}