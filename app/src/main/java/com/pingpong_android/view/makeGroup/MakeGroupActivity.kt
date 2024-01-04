package com.pingpong_android.view.makeGroup

import android.os.Bundle
import com.pingpong_android.R
import com.pingpong_android.base.BaseActivity
import com.pingpong_android.databinding.ActivityMakeGroupBinding

class MakeGroupActivity : BaseActivity<ActivityMakeGroupBinding>(R.layout.activity_make_group) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = MakeGroupViewModel()
        binding.activity = this

    }
}