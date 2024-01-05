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

        initSubscribe()
        initAdapter()
        searchEvent()

        requestSearchLog()  // 검색 기록 로그
    }

    private fun initAdapter() {
        // log Adapter
        // todo : log adapter

        // search Adapter
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

    private fun initSubscribe() {
        subscribeSearchLog()
        subscribeSearchResult()
    }

    private fun subscribeSearchLog() {
        binding.viewModel!!.logList.observe(this, Observer {
            if (it.isSuccess && it.logList.isNotEmpty()) {
                setViewType(beforeSearch = true, hasResult = true)
                // todo
            } else {
                setViewType(beforeSearch = true, hasResult = true)
                // todo
            }
        })
    }

    private fun subscribeSearchResult() {
        binding.viewModel!!.userList.observe(this, Observer {
            if (it.isSuccess && it.friendList.isNotEmpty()) {
                setViewType(beforeSearch = false, hasResult = true)

                friendsAdapter.addList(it.friendList)
            } else {
                setViewType(beforeSearch = false, hasResult = true)
                // todo
            }
        })
    }

    private fun setViewType(beforeSearch : Boolean, hasResult : Boolean) {
        if (beforeSearch) {
            binding.friendRv.visibility = View.GONE
            binding.noUserList.visibility = View.GONE

            binding.searchLogLayout.visibility = View.VISIBLE
            if (hasResult) {
                binding.noRecentSearch.visibility = View.GONE
                binding.logRv.visibility = View.VISIBLE
            } else {
                binding.noRecentSearch.visibility = View.VISIBLE
                binding.logRv.visibility = View.GONE
            }
        } else {
            binding.searchLogLayout.visibility = View.GONE
            binding.noRecentSearch.visibility = View.GONE
            binding.logRv.visibility = View.GONE

            if (hasResult) {
                binding.friendRv.visibility = View.VISIBLE
                binding.noUserList.visibility = View.GONE
            } else {
                binding.friendRv.visibility = View.GONE
                binding.noUserList.visibility = View.VISIBLE
            }
        }
    }

    fun addSearchLog(id: String) {
        binding.viewModel!!.addSearchLog(prefsUtil.getBearerToken(), id.toLong())
    }

    private fun requestSearchLog() {
        // 검색 로그 조회
        binding.viewModel!!.requestSearchLog(prefsUtil.getBearerToken())
    }

    fun deleteAllSearchLog() {

    }
}