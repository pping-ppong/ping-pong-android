package com.pingpong_android.view.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.pingpong_android.R
import com.pingpong_android.base.BaseActivity
import com.pingpong_android.databinding.ActivitySearchBinding
import com.pingpong_android.utils.PreferenceUtil
import com.pingpong_android.view.adapter.FriendsAdapter

class SearchActivity : BaseActivity<ActivitySearchBinding>(R.layout.activity_search){

    private lateinit var prefsUtil : PreferenceUtil
    private var friendsAdapter = FriendsAdapter(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = SearchViewModel()
        binding.activity = this

        prefsUtil = PreferenceUtil(applicationContext)

        subscribeSearchResult()
        initAdapter()
        searchEvent()
    }

    private fun initAdapter() {
        val friendLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        friendsAdapter.setSearchActivity(this)

        binding.friendRv.apply {
            layoutManager = friendLayoutManager
            adapter = friendsAdapter
        }
    }

    private fun searchEvent() {
        binding.nickNmEt.addTextChangedListener(textWatcher)
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            binding.viewModel!!.searchUserWithNickNm(prefsUtil.getBearerToken(), s.toString())
        }
    }

    private fun subscribeSearchResult() {
        binding.viewModel!!.userList.observe(this, Observer {
            if (it.isSuccess && it.friendList.isNotEmpty()) {
                binding.friendRv.visibility = View.VISIBLE

                friendsAdapter.addList(it.friendList)
            } else {
                binding.friendRv.visibility = View.GONE
            }
        })
    }
}