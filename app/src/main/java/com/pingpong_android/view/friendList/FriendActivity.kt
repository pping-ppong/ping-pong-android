package com.pingpong_android.view.friendList

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.pingpong_android.R
import com.pingpong_android.base.BaseActivity
import com.pingpong_android.base.Constants
import com.pingpong_android.databinding.ActivityFriendBinding
import com.pingpong_android.view.profile.ProfileActivity

class FriendActivity : BaseActivity<ActivityFriendBinding>(R.layout.activity_friend, TransitionMode.RIGHT) {

    private var friendsAdapter = FriendsAdapter(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = FriendViewModel()
        binding.activity = this

        initAdapter()
        initRequest()
        binding.topPanel.setLeftClickListener(listener = {onBackPressed()})
    }

    private fun initAdapter() {
        val friendLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        friendsAdapter.setFriendActivity(this)

        binding.friendRv.apply {
            layoutManager = friendLayoutManager
            adapter = friendsAdapter
        }
    }

    private fun initRequest() {
        subscribeFriendList()
        binding.viewModel!!.requestUserFriendList(prefs.getBearerToken())
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

    fun goToUserProfile(memberId : Long) {
        val intent = Intent(this, ProfileActivity::class.java)
        intent.putExtra(Constants.INTENT_EXTRA_MEMBER_ID, memberId)
        startActivity(intent)
    }
}