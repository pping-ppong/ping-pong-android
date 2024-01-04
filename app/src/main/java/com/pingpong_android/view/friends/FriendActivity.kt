package com.pingpong_android.view.friends

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.pingpong_android.R
import com.pingpong_android.base.BaseActivity
import com.pingpong_android.databinding.ActivityFriendBinding
import com.pingpong_android.utils.PreferenceUtil
import com.pingpong_android.view.adapter.FriendsAdapter


class FriendActivity : BaseActivity<ActivityFriendBinding>(R.layout.activity_friend) {

    private lateinit var prefsUtil : PreferenceUtil
    private var friendsAdapter = FriendsAdapter(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = FriendViewModel()
        binding.activity = this

        prefsUtil = PreferenceUtil(applicationContext)

        initAdapter()
        initRequest()
    }

    private fun initAdapter() {
        val friendLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        friendsAdapter.setActivity(this)

        binding.friendRv.apply {
            layoutManager = friendLayoutManager
            adapter = friendsAdapter
        }
    }

    private fun initRequest() {
        subscribeFriendList()
        binding.viewModel!!.requestUserFriendList(prefsUtil.getBearerToken())
    }

    private fun subscribeFriendList() {
        binding.viewModel!!.friendList.observe(this, Observer {
            if (it.isSuccess && it.friendList.isNotEmpty()) {
                binding.friendRv.visibility = View.VISIBLE
                binding.noFriendList.visibility = View.GONE

                friendsAdapter.addList(it.friendList)
            } else {
                binding.friendRv.visibility = View.GONE
                binding.noFriendList.visibility = View.VISIBLE
            }
        })
    }
}