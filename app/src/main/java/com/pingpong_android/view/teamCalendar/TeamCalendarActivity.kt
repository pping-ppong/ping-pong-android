package com.pingpong_android.view.teamCalendar

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.bumptech.glide.Glide
import com.pingpong_android.R
import com.pingpong_android.base.BaseActivity
import com.pingpong_android.base.Constants.Companion.INTENT_EXTRA_TEAM_DTO
import com.pingpong_android.databinding.ActivityTeamCalendarBinding
import com.pingpong_android.model.TeamDTO
import com.pingpong_android.view.adapter.MemberHorizontalAdapter
import com.pingpong_android.view.teamCalendar.adapter.TeamCalendarAdapter

class TeamCalendarActivity : BaseActivity<ActivityTeamCalendarBinding>(R.layout.activity_team_calendar, TransitionMode.RIGHT) {

    private lateinit var teamDTO: TeamDTO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = TeamCalendarViewModel()
        binding.activity = this

        initView()
        initAdapter()
    }

    private fun initView() {
        teamDTO = intent.getSerializableExtra(INTENT_EXTRA_TEAM_DTO) as TeamDTO

        binding.topPanel.setTitle(teamDTO.teamName)
        binding.topPanel.setLeftClickListener(listener = { onBackPressed() })
    }

    private fun initAdapter() {
        // 캘린더
        val monthListManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val monthListAdapter = TeamCalendarAdapter()
        monthListAdapter.setActivity(this)

        binding.calender.apply {
            layoutManager = monthListManager
            adapter = monthListAdapter
            scrollToPosition(Int.MAX_VALUE/2)
        }
        val snap = PagerSnapHelper()
        snap.attachToRecyclerView(binding.calender)

        // 멤버
        val membersLayoutManager = LinearLayoutManager(this)
        val memberHorizontalAdapter = MemberHorizontalAdapter(teamDTO.memberList)

        binding.memberRv.apply {
            layoutManager = membersLayoutManager
            adapter = memberHorizontalAdapter
        }
    }

    fun onClickInputText(isAdding : Boolean) {
        // 할 일 입력
        if (isAdding) {
            binding.textInputLayout.visibility = View.VISIBLE

            // 텍스트 작성에 포커스 주기
            binding.todoTxt.requestFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.todoTxt, 0)
        }
        // 입력 멈춤
        else {
            binding.textInputLayout.visibility = View.GONE
            binding.profileImg.visibility = View.GONE
            binding.defaultImage.visibility = View.VISIBLE

            // 작성값 초기화
            binding.todoTxt.setText("")
            binding.todoDate.text = ""
            binding.todoMemberName.text = ""

            binding.defaultImage.setImageResource(R.drawable.ic_profile)
            Glide.with(this).clear(binding.profileImg)

            // 키보드 내리기
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(window.decorView.applicationWindowToken, 0)
        }
    }

    fun onClickDateMember() {
        Toast.makeText(this, "date & member", Toast.LENGTH_SHORT).show()
//        binding.textInputLayout.visibility = View.GONE
        // todo : 팝업창 (설정값 전달_nullable, 설정값 받기, textlayout visible + data set)
    }

    fun onClickAddTodo() {
        Toast.makeText(this, "send", Toast.LENGTH_SHORT).show()
    }

    private fun checkAddable() {

    }
}