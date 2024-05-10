package com.pingpong_android.view.friendList

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
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
        initSubscribe()
        binding.topPanel.setLeftClickListener(listener = {onBackPressed()})
    }

    override fun onResume() {
        super.onResume()
        binding.viewModel!!.requestUserFriendList(prefs.getBearerToken())
    }

    private fun initAdapter() {
        val friendLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        friendsAdapter.setFriendActivity(this)

        binding.friendRv.apply {
            layoutManager = friendLayoutManager
            adapter = friendsAdapter
        }
    }

    private fun initSubscribe() {
        subscribeFriendList()
        subscribeDeleteFriendShip()
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

    private fun subscribeDeleteFriendShip() {
        binding.viewModel!!.deleteResult.observe(this, Observer {
            if (it.isSuccess) {
                binding.viewModel!!.requestUserFriendList(prefs.getBearerToken())
            }
            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
        })
    }

    fun deleteFriendShip(memberId: Long) {
        binding.viewModel!!.deleteFriendShip(prefs.getBearerToken(), memberId)
    }

    fun goToUserProfile(memberId : Long) {
        val intent = Intent(this, ProfileActivity::class.java)
        intent.putExtra(Constants.INTENT_EXTRA_MEMBER_ID, memberId)
        startActivity(intent)
    }
}