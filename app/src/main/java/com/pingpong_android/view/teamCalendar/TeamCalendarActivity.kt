package com.pingpong_android.view.teamCalendar

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.bumptech.glide.Glide
import com.pingpong_android.R
import com.pingpong_android.base.BaseActivity
import com.pingpong_android.base.Constants.Companion.INTENT_EXTRA_TEAM_DTO
import com.pingpong_android.databinding.ActivityTeamCalendarBinding
import com.pingpong_android.model.MemberDTO
import com.pingpong_android.model.TeamDTO
import com.pingpong_android.model.TodoDTO
import com.pingpong_android.view.adapter.MemberHorizontalAdapter
import com.pingpong_android.view.teamCalendar.adapter.TeamCalendarAdapter

class TeamCalendarActivity : BaseActivity<ActivityTeamCalendarBinding>(R.layout.activity_team_calendar, TransitionMode.RIGHT) {

    private lateinit var teamDTO: TeamDTO

    // add todo_
    private lateinit var memberDTO: MemberDTO
    private lateinit var date_todo : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = TeamCalendarViewModel()
        binding.activity = this

        memberDTO = MemberDTO(49)
        date_todo = "2024-01-24"

        initView()
        initAdapter()

        binding.viewModel!!.requestMonthAchievement(
            prefs.getBearerToken(), teamDTO.teamId, "2024-01-01", "2024-01-31")
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initView() {
        teamDTO = intent.getSerializableExtra(INTENT_EXTRA_TEAM_DTO) as TeamDTO

        binding.topPanel.setTitle(teamDTO.teamName)
        binding.topPanel.setLeftClickListener(listener = { onBackPressed() })

        // 이벤트 처리
        binding.bottomSheetLayout.setOnTouchListener { _, _ -> true }   // 뒷 배경으로 클릭모션 넘어가지 않도록
        binding.todoTxt.addTextChangedListener(textWatcher)
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            checkAddable()
        }
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

    private fun onClickAddTodo() {
        var todoDTO = TodoDTO(binding.todoTxt.text.toString())
        todoDTO.date = date_todo
        todoDTO.managerId = memberDTO.memberId
        binding.viewModel!!.requestAddTodo(prefs.getBearerToken(), teamDTO.teamId, todoDTO)
    }

    private fun checkAddable() {
        if (!binding.todoTxt.text.isNullOrEmpty() && !date_todo.isNullOrEmpty() && memberDTO != null) {
            binding.btnRequestAddTodo.setImageDrawable(this.getDrawable(R.drawable.ic_send_enabled))
            binding.btnRequestAddTodo.setOnClickListener { onClickAddTodo() }
        } else {
            binding.btnRequestAddTodo.setImageDrawable(this.getDrawable(R.drawable.ic_send_disabled))
            binding.btnRequestAddTodo.setOnClickListener(null)
        }
    }
}