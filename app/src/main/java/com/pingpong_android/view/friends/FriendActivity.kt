package com.pingpong_android.view.friends

import android.os.Bundle
import com.pingpong_android.R
import com.pingpong_android.base.BaseActivity
import com.pingpong_android.databinding.ActivityFriendBinding


class FriendActivity : BaseActivity<ActivityFriendBinding>(R.layout.activity_friend) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = FriendViewModel()
        binding.activity = this
    }
}