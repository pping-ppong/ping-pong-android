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
import com.pingpong_android.model.AchieveDTO
import com.pingpong_android.model.TeamDTO
import com.pingpong_android.model.UserDTO
import com.pingpong_android.view.main.adapter.CalendarAdapter
import com.pingpong_android.view.myPage.MyPageActivity
import com.pingpong_android.view.notice.NoticeActivity
import com.pingpong_android.view.search.SearchActivity
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Calendar
import java.util.Date

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private lateinit var userDTO: UserDTO
    var date: LocalDate = LocalDate.now()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = MainViewModel()
        binding.activity = this

        initUserDTO()
        initSubscribe()

        setClickListener()
        initRequest()
    }

    private fun initUserDTO() {
        userDTO = prefs.getUser()
        prefs.saveBearerToken(userDTO.accessToken)

        if (userDTO.profileImage.isNotEmpty()) {
            binding.defaultImage.visibility = View.GONE
            Glide.with(binding.btnMypage)
                .load(userDTO.profileImage)
                .into(binding.btnMypage)
            binding.btnMypage.clipToOutline = true
        } else {
            binding.defaultImage.visibility = View.VISIBLE
            Glide.with(binding.btnMypage).clear(binding.btnMypage)
        }
    }

    private fun initAdapter(achieves: List<AchieveDTO>) {
        val monthListManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val monthListAdapter = CalendarAdapter()
        monthListAdapter.setMainActivity(this)
        monthListAdapter.addAchieveList(achieves)

        binding.calender.apply {
            layoutManager = monthListManager
            adapter = monthListAdapter
            scrollToPosition(Int.MAX_VALUE/2)
        }
    }

    private fun initPlanAdapter(plans : List<TeamDTO>) {

    }

    private fun setClickListener() {
        binding.btnMypage.setOnClickListener { goToMyPage() }
        binding.btnSearch.setOnClickListener { goToSearch()}
        binding.btnAlarm.setOnClickListener { goToNotice() }
    }

    private fun initRequest() {
        requestCalAchieveNow()
        binding.viewModel!!.requestPlans(prefs.getBearerToken(), date.toString())
        binding.viewModel!!.requestUserInfo(prefs.getBearerToken(), userDTO)
        binding.viewModel!!.requestUnReadNotice(prefs.getBearerToken())
    }

    private fun requestCalAchieveNow() {
        binding.viewModel!!.requestMonthAchievement(prefs.getBearerToken(),
                    startDate = date.withDayOfMonth(1).toString(),
                    endDate = date.withDayOfMonth(date.lengthOfMonth()).toString())
    }

    fun requestCalAchieveNow(position : Int) {
        if (position == 1)
            date.plusMonths(1)
        else
            date.minusMonths(1)

        binding.viewModel!!.requestMonthAchievement(prefs.getBearerToken(),
            startDate = date.withDayOfMonth(1).toString(),
            endDate = date.withDayOfMonth(date.lengthOfMonth()).toString())
    }

    private fun initSubscribe() {
        subscribeAchieve()
        subscribeNoticeState()
        subscribeUserInfo()
        subscribePlans()
    }

    private fun subscribeAchieve() {
        binding.viewModel!!.achieveResult.observe(this, Observer {
            if (it.isSuccess && !it.achieveList.isNullOrEmpty()) {
                initAdapter(it.achieveList)
            } else {
                initAdapter(emptyList())
            }
        })
    }

    private fun subscribePlans() {
        binding.viewModel!!.plansResult.observe(this, Observer {
            if (it.isSuccess && !it.teamList.isNullOrEmpty()) {
                initPlanAdapter(it.teamList)
            } else {
                initPlanAdapter(emptyList())
            }
        })
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

    private fun subscribeUserInfo() {
        binding.viewModel!!.userData.observe(this, Observer {
            if (it.isSuccess) {
                // 유저 정보 조회 성공
                initView(it.userDTO)
            } else {
                // 유저 정보 조회 실패
                initView(userDTO)
            }
        })
    }

    private fun initView(user : UserDTO) {
        if (user.profileImage.isNotEmpty()) {
            binding.defaultImage.visibility = View.GONE
            Glide.with(binding.btnMypage)
                .load(user.profileImage)
                .into(binding.btnMypage)
            binding.btnMypage.clipToOutline = true
        } else {
            binding.defaultImage.visibility = View.VISIBLE
            Glide.with(binding.btnMypage).clear(binding.btnMypage)
        }
    }

    fun requestPlans(day : Date) {
        var tmpDate : LocalDate = day.toInstant()
                                        .atZone(ZoneId.systemDefault())
                                        .toLocalDate()
        binding.viewModel!!.requestPlans(prefs.getBearerToken(), tmpDate.toString())
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