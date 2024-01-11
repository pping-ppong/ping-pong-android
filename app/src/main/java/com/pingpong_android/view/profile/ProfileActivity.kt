package com.pingpong_android.view.profile

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.pingpong_android.R
import com.pingpong_android.base.BaseActivity
import com.pingpong_android.base.Constants.Companion.INTENT_EXTRA_MEMBER_ID
import com.pingpong_android.databinding.ActivityOthersProfileBinding
import com.pingpong_android.model.UserDTO
import com.pingpong_android.utils.PreferenceUtil

class ProfileActivity  : BaseActivity<ActivityOthersProfileBinding>(R.layout.activity_others_profile) {

    companion object {
        lateinit var prefs: PreferenceUtil
        var memberId : Long = -1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = ProfileViewModel()
        binding.activity = this

        prefs = PreferenceUtil(applicationContext)
        memberId = intent.getLongExtra(INTENT_EXTRA_MEMBER_ID, -1)

        initView()
        initSubscribe()
        binding.viewModel!!.requestOthersProfile(prefs.getBearerToken(), memberId)
    }

    private fun initView() {
        binding.btnFriend.setOnClickListener {
            binding.viewModel!!.requestFriendShip(prefs.getBearerToken(), prefs.getId().toLong(), memberId)
        }
    }

    private fun initSubscribe() {
        subscribeOthersProfile()
        subscribeApplyFriendShip()
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
        binding.viewModel!!.result.observe(this, Observer {

        })
    }

    private fun friendView(user : UserDTO) {
        binding.userNm.text = user.nickName
        binding.cnt.text = String.format(getString(R.string.friend_num), user.friendCnt)

        if (user.profileImage.isNotEmpty()) {
            binding.defaultImage.visibility = View.GONE
            Glide.with(binding.image).load(user.profileImage)
                .error(R.drawable.ic_profile_popcorn)   // 오류일 경우
                .fallback(R.drawable.ic_profile_popcorn)    // Null인 경우
                .placeholder(R.drawable.ic_profile_popcorn) // 로드 전
                .into(binding.image)
            binding.image.clipToOutline = true
        } else {
            binding.defaultImage.visibility = View.VISIBLE
            Glide.with(binding.image).clear(binding.image)
        }
    }
}