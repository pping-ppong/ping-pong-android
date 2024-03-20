package com.pingpong_android.view.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.pingpong_android.R
import com.pingpong_android.base.BaseActivity
import com.pingpong_android.base.Constants.Companion.INTENT_EXTRA_MEMBER_ID
import com.pingpong_android.databinding.ActivitySearchBinding
import com.pingpong_android.view.profile.ProfileActivity

class SearchActivity : BaseActivity<ActivitySearchBinding>(R.layout.activity_search){

    private var searchAdapter = SearchAdapter(emptyList())
    private var logAdapter = LogAdapter(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = SearchViewModel()
        binding.activity = this

        initSubscribe()
        initAdapter()
        searchEvent()

        requestSearchLog()  // 검색 기록 로그
    }

    private fun initAdapter() {
        // log Adapter
        val logLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        logAdapter.setActivity(this)

        binding.logRv.apply {
            layoutManager = logLayoutManager
            adapter = logAdapter
        }

        // search Adapter
        val friendLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        searchAdapter.setSearchActivity(this)

        binding.friendRv.apply {
            layoutManager = friendLayoutManager
            adapter = searchAdapter
        }
    }

    private fun searchEvent() {
        binding.nickNmEt.setOnEditorActionListener{ textView, action, event ->
            var handled = false
            if (action == EditorInfo.IME_ACTION_DONE) {
                searchKeyword(binding.nickNmEt.text.toString())

                // 키보드 내리기
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(window.decorView.applicationWindowToken, 0)

                handled = true
            }
            handled
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
                logAdapter.addList(it.logList)
            } else {
                setViewType(beforeSearch = true, hasResult = true)
                logAdapter.addList(emptyList())
            }
        })
    }

    private fun subscribeSearchResult() {
        binding.viewModel!!.userList.observe(this, Observer {
            if (it.isSuccess && it.friendList.isNotEmpty()) {
                setViewType(beforeSearch = false, hasResult = true)
                searchAdapter.addList(it.friendList)

                binding.friendRv.visibility = View.VISIBLE
                binding.noUserList.visibility = View.GONE
            } else {
                setViewType(beforeSearch = false, hasResult = true)
                searchAdapter.addList(emptyList())

                binding.friendRv.visibility = View.GONE
                binding.noUserList.visibility = View.VISIBLE
            }
        })
    }

    private fun setViewType(beforeSearch : Boolean, hasResult : Boolean) {
        if (beforeSearch) {
            // 검색 전 뷰
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
            // 검색 중 뷰
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

    fun searchKeyword(keyword : String) {
        binding.viewModel!!.searchUserWithNickNm(prefs.getBearerToken(), keyword)
    }

    fun addSearchLog(memberId : Long) {
        binding.viewModel!!.addSearchLog(prefs.getBearerToken(), memberId)
        goToUserProfile(memberId)
    }

    private fun requestSearchLog() {
        // 검색 로그 조회
        binding.viewModel!!.requestSearchLog(prefs.getBearerToken())
    }

    fun deleteAllSearchLog() {

    }

    private fun goToUserProfile(memberId : Long) {
        val intent = Intent(this, ProfileActivity::class.java)
        intent.putExtra(INTENT_EXTRA_MEMBER_ID, memberId)
        startActivity(intent)
    }
}