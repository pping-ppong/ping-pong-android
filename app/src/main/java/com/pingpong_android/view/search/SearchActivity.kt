package com.pingpong_android.view.search

import android.os.Bundle
import com.pingpong_android.R
import com.pingpong_android.base.BaseActivity
import com.pingpong_android.databinding.ActivitySearchBinding

class SearchActivity : BaseActivity<ActivitySearchBinding>(R.layout.activity_search){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = SearchViewModel()
        binding.activity = this
    }

}