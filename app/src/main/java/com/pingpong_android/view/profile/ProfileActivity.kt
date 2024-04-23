package com.pingpong_android.view.profile

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.pingpong_android.R
import com.pingpong_android.base.BaseActivity
import com.pingpong_android.base.Constants.Companion.INTENT_EXTRA_MEMBER_ID
import com.pingpong_android.base.Status
import com.pingpong_android.databinding.ActivityOthersProfileBinding
import com.pingpong_android.model.UserDTO

class ProfileActivity  : BaseActivity<ActivityOthersProfileBinding>(R.layout.activity_others_profile, TransitionMode.RIGHT) {

    var memberId : Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = ProfileViewModel()
        binding.activity = this

        memberId = intent.getLongExtra(INTENT_EXTRA_MEMBER_ID, -1)

        initView()
        initSubscribe()
        binding.viewModel!!.requestOthersProfile(prefs.getBearerToken(), memberId)
    }

    private fun initView() {
        binding.topPanel.setLeftClickListener(listener = {onBackPressed()})
    }

    private fun initSubscribe() {
        subscribeOthersProfile()
        subscribeApplyFriendShip()
        subscribeDeleteFriendShip()
        subscribeAlarmSend()
    }

    private fun subscribeOthersProfile() {
        binding.viewModel!!.userResult.observe(this, Observer {
            if (it.isSuccess) {
                friendView(it.userDTO)
            } else {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun subscribeApplyFriendShip() {
        binding.viewModel!!.applyResult.observe(this, Observer {
            if (it.isSuccess) {
                binding.viewModel!!.requestAlarmFriend(prefs.getBearerToken(), memberId)
                Toast.makeText(this@ProfileActivity, it.message, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this@ProfileActivity, it.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun subscribeAlarmSend() {
        binding.viewModel!!.alarmResult.observe(this, Observer {
            if (it.isSuccess) {
                binding.viewModel!!.requestOthersProfile(prefs.getBearerToken(), memberId)
            }
        })
    }

    private fun subscribeDeleteFriendShip() {
        binding.viewModel!!.deleteResult.observe(this, Observer {
            if (it.message.isNotEmpty())
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
        })
    }

    private fun friendView(user : UserDTO) {
        binding.topPanel.setTitle(user.nickName)
        binding.cnt.text = String.format(getString(R.string.friend_num), user.friendCnt)

        if (user.profileImage.isNotEmpty()) {
            binding.defaultImage.visibility = View.GONE
            Glide.with(binding.image).load(user.profileImage)
                .into(binding.image)
            binding.image.clipToOutline = true
        } else {
            binding.defaultImage.visibility = View.VISIBLE
            Glide.with(binding.image).clear(binding.image)
        }

        when (user.friendStatus) {
            Status.ACTIVE -> {
                binding.btnFriend.text = getString(R.string.friend)
                binding.btnFriend.setTextColor(getColor(R.color.black))
                binding.btnFriend.background = getDrawable(R.drawable.back_white_stroke_gray_30dp)
                binding.btnFriend.setOnClickListener { deleteFriendShip() }
            }
            Status.WAIT -> {
                binding.btnFriend.text = getString(R.string.wait)
                binding.btnFriend.setTextColor(getColor(R.color.black))
                binding.btnFriend.background = getDrawable(R.drawable.back_white_stroke_gray_30dp)
                binding.btnFriend.setOnClickListener { null }
            }
            else -> {
                binding.btnFriend.text = getString(R.string.add_friend)
                binding.btnFriend.setTextColor(getColor(R.color.white))
                binding.btnFriend.background = getDrawable(R.drawable.back_black_30dp)
                binding.btnFriend.setOnClickListener{ requestFriendShip() }
            }
        }
    }

    private fun requestFriendShip() {
        binding.viewModel!!.requestFriendShip(prefs.getBearerToken(), prefs.getId().toLong(), memberId)
    }

    private fun deleteFriendShip() {
        binding.viewModel!!.deleteFriendShip(prefs.getBearerToken(), memberId)
    }
}