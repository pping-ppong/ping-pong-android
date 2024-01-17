package com.pingpong_android.view.groupList

import android.content.Intent
import android.os.Bundle
import com.pingpong_android.R
import com.pingpong_android.base.BaseActivity
import com.pingpong_android.databinding.ActivityGroupListBinding
import com.pingpong_android.view.main.MainActivity

class GroupListActivity : BaseActivity<ActivityGroupListBinding>(R.layout.activity_group_list, TransitionMode.RIGHT) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = GroupListViewModel()
        binding.activity = this

        setClickListener()
    }

    private fun setClickListener() {
        binding.topPanel.setLeftClickListener(listener = {onBackPressed()})
    }

    private fun goToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}