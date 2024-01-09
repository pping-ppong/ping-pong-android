package com.pingpong_android.view.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.bumptech.glide.Glide
import com.pingpong_android.R
import com.pingpong_android.base.BaseActivity
import com.pingpong_android.databinding.ActivityMainBinding
import com.pingpong_android.model.UserDTO
import com.pingpong_android.utils.PreferenceUtil
import com.pingpong_android.view.login.LoginActivity
import com.pingpong_android.view.main.adapter.CalendarAdapter
import com.pingpong_android.view.myPage.MyPageActivity
import com.pingpong_android.view.notice.NoticeActivity
import com.pingpong_android.view.search.SearchActivity

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    companion object {
        lateinit var prefs: PreferenceUtil
        private lateinit var userDTO: UserDTO
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = MainViewModel()
        binding.activity = this

        initUserDTO()
        initSubscribe()
        initAdapter()

        setClickListener()
        binding.viewModel!!.requestNotReadNotice(prefs.getBearerToken())
    }

    private fun initUserDTO() {
        prefs = PreferenceUtil(applicationContext)
        userDTO = prefs.getUser()
        prefs.saveBearerToken(userDTO.accessToken)

        if (userDTO.profileImage.isNotEmpty()) {
            binding.defaultImage.visibility = View.GONE
            Glide.with(binding.btnMypage).load(userDTO.profileImage)
                .error(R.drawable.ic_profile_popcorn)   // 오류일 경우
                .fallback(R.drawable.ic_profile_popcorn)    // Null인 경우
                .placeholder(R.drawable.ic_profile_popcorn) // 로드 전
                .into(binding.btnMypage)
            binding.btnMypage.clipToOutline = true
        } else {
            binding.defaultImage.visibility = View.VISIBLE
            Glide.with(binding.btnMypage).clear(binding.btnMypage)
        }
    }

    private fun initAdapter() {
        val monthListManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val monthListAdapter = CalendarAdapter()
        monthListAdapter.setMainActivity(this)

        binding.calender.apply {
            layoutManager = monthListManager
            adapter = monthListAdapter
            scrollToPosition(Int.MAX_VALUE/2)
        }
        val snap = PagerSnapHelper()
        snap.attachToRecyclerView(binding.calender)
    }

    private fun setClickListener() {
        binding.btnMypage.setOnClickListener { goToMyPage() }
        binding.btnSearch.setOnClickListener { goToSearch()}
        binding.btnAlarm.setOnClickListener { goToNotice() }
    }

    private fun initSubscribe() {
        subscribeNoticeState()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun subscribeNoticeState() {
        binding.viewModel!!.noticeState.observe(this, Observer {
            if (it.isSuccess) {
                if (it.message.equals("읽지 않은 알림이 존재하지 합니다"))
                    binding.btnAlarm.setImageDrawable(getDrawable(R.drawable.ic_alarm_on))
                else if (it.message.equals("모든 알림을 읽었습니다"))
                    binding.btnAlarm.setImageDrawable(getDrawable(R.drawable.ic_alarm_off))
                else
                    binding.btnAlarm.setImageDrawable(getDrawable(R.drawable.ic_alarm_setting))
            } else {
                binding.btnAlarm.setImageDrawable(getDrawable(R.drawable.ic_alarm_setting))
            }
        })
    }

    private fun goToMyPage() {
        val intent = Intent(this, MyPageActivity::class.java)
        startActivity(intent)
    }

    private fun goToSearch() {
        val intent = Intent(this, SearchActivity::class.java)
        startActivity(intent)
    }

    private fun goToNotice() {
        val intent = Intent(this, NoticeActivity::class.java)
        startActivity(intent)
    }
}