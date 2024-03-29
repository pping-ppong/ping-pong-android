package com.pingpong_android.view.teamCalendar

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.pingpong_android.R
import com.pingpong_android.base.BaseActivity
import com.pingpong_android.base.Constants
import com.pingpong_android.base.Constants.Companion.INTENT_EXTRA_TEAM_DTO
import com.pingpong_android.databinding.ActivityTeamCalendarBinding
import com.pingpong_android.layout.DateMemberSetDialog
import com.pingpong_android.layout.ModalBottomSheetDialog
import com.pingpong_android.model.MemberDTO
import com.pingpong_android.model.TeamDTO
import com.pingpong_android.model.TodoDTO
import com.pingpong_android.view.adapter.MemberHorizontalAdapter
import com.pingpong_android.view.join.JoinActivity
import com.pingpong_android.view.teamCalendar.adapter.TeamCalendarAdapter
import com.pingpong_android.view.teamCalendar.adapter.TeamTodoAdapter
import com.pingpong_android.view.teamMemList.TeamMemberActivity
import java.lang.NullPointerException
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

class TeamCalendarActivity : BaseActivity<ActivityTeamCalendarBinding>(R.layout.activity_team_calendar, TransitionMode.RIGHT) {

    private lateinit var teamDTO: TeamDTO
    private lateinit var memberDTO: MemberDTO   // 할 일 생성 시 담당 멤버
    private lateinit var date_for_plan : String // 할 일 생성 시 날짜

    private var monthListAdapter = TeamCalendarAdapter()
    private var todoListAdapter = TeamTodoAdapter()

    var myMemberId : Long = 0
    private var date_for_cal: LocalDate = LocalDate.now().withDayOfMonth(1)
    private var date_for_day: LocalDate = LocalDate.now()

    private var menuList : List<String> = listOf("넘기기", "버리기")
    private var todoTxtCheck : Boolean = false
    private var todoMemberCheck : Boolean = false
    private var todoDateCheck : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = TeamCalendarViewModel()
        binding.activity = this

        myMemberId = prefs.getId().toLong()

        initView()
        initSubscribe()
        initAdapter()
        initRequest()
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
            checkTodoText()
        }
    }

    private fun initAdapter() {
        // 캘린더
        val monthListManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        monthListAdapter.setDateToCalendar(date_for_cal)
        monthListAdapter.addAchieveList(emptyList())
        monthListAdapter.setActivity(this)

        binding.calender.apply {
            layoutManager = monthListManager
            adapter = monthListAdapter
            scrollToPosition(Int.MAX_VALUE/2)
        }

        // 할 일
        val planListManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        todoListAdapter.setActivity(this)
        todoListAdapter.addPlanList(emptyList())

        binding.teamPlanRv.apply {
            layoutManager = planListManager
            adapter = todoListAdapter
        }

        // 멤버
        val membersLayoutManager = LinearLayoutManager(this)
        val memberHorizontalAdapter = MemberHorizontalAdapter(teamDTO.memberList, false)

        binding.memberRv.apply {
            layoutManager = membersLayoutManager
            adapter = memberHorizontalAdapter
        }
    }

    private fun initSubscribe() {
        subscribeAchieve()
        subscribePlans()
        subscribeComplete()
        subscribeAddTodo()
    }

    // 달성률 - request 결과
    private fun subscribeAchieve() {
        binding.viewModel!!.achieveResult.observe(this, androidx.lifecycle.Observer {
            if (it.isSuccess && !it.achieveList.isNullOrEmpty()) {
                monthListAdapter.setDateToCalendar(date_for_cal)
                monthListAdapter.addAchieveList(it.achieveList)
                monthListAdapter.notifyDataSetChanged()
            } else {
                monthListAdapter.setDateToCalendar(date_for_cal)
                monthListAdapter.addAchieveList(emptyList())
                monthListAdapter.notifyDataSetChanged()
            }
        })
    }

    // 할 일 - request 결과
    private fun subscribePlans() {
        binding.viewModel!!.plansResult.observe(this, androidx.lifecycle.Observer {
            if (it.isSuccess && !it.team.planList.isNullOrEmpty()) {
                todoListAdapter.addPlanList(it.team.planList)
            } else {
                todoListAdapter.addPlanList(emptyList())
            }
        })
    }

    private fun subscribeAddTodo() {
        binding.viewModel!!.addTodoResult.observe(this, androidx.lifecycle.Observer {
            if (it.isSuccess) {
                initRequest()
                onClickInputText(false)
            } else {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    // 할 일 완료/미완료 - request 결과
    private fun subscribeComplete() {
        binding.viewModel!!.planRequestResult.observe(this, androidx.lifecycle.Observer {
            if (it.isSuccess) {
                binding.viewModel!!.requestMonthAchievement(prefs.getBearerToken(), teamDTO.teamId,
                    startDate = date_for_cal,
                    endDate = date_for_cal.withDayOfMonth(date_for_cal.lengthOfMonth()))
                binding.viewModel!!.requestPlans(prefs.getBearerToken(), teamDTO.teamId, date_for_day.toString())
            }
        })
    }

    private fun initRequest() {
        binding.viewModel!!.requestMonthAchievement(prefs.getBearerToken(), teamDTO.teamId,
            startDate = date_for_cal,
            endDate = date_for_cal.withDayOfMonth(date_for_cal.lengthOfMonth()))
        binding.viewModel!!.requestPlans(prefs.getBearerToken(), teamDTO.teamId, date_for_day.toString())
        date_for_plan = date_for_day.toString()
    }

    // 달성률 조회 (날짜 이동)
    fun requestCalAchieveNow(position : Int) {
        if (position == 1)
            date_for_cal = date_for_cal.plusMonths(1)
        else
            date_for_cal = date_for_cal.minusMonths(1)

        binding.viewModel!!.requestMonthAchievement(prefs.getBearerToken(), teamDTO.teamId,
            startDate = date_for_cal,
            endDate = date_for_cal.withDayOfMonth(date_for_cal.lengthOfMonth()))
    }

    // 할 일 불러오기
    fun requestPlans(day : Date) {
        date_for_day = day.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
        date_for_plan = date_for_day.toString()
        binding.viewModel!!.requestPlans(prefs.getBearerToken(), teamDTO.teamId, date_for_day.toString())
    }

    // 할 일 완료/미완료 처리
    fun requestPlanComplete(isComplete : Boolean, planId: Long) {
        if (isComplete) {   // 완료 표시
            binding.viewModel!!.requestPlanComplete(prefs.getBearerToken(), teamDTO.teamId, planId)
        } else {    // 미완료 표시
            binding.viewModel!!.requestPlanIncomplete(prefs.getBearerToken(), teamDTO.teamId, planId)
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

            date_for_plan = ""
            memberDTO = MemberDTO(-1)

            binding.defaultImage.setImageResource(R.drawable.ic_profile)
            Glide.with(this).clear(binding.profileImg)

            // 키보드 내리기
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(window.decorView.applicationWindowToken, 0)
        }
    }

    private fun onClickAddTodo() {
        var todoDTO = TodoDTO(binding.todoTxt.text.toString())
        todoDTO.date = date_for_plan
        todoDTO.managerId = memberDTO.memberId
        binding.viewModel!!.requestAddTodo(prefs.getBearerToken(), teamDTO.teamId, todoDTO)
    }

    private fun checkTodoText() {
        if (!binding.todoTxt.text.isNullOrEmpty())
            todoTxtCheck = true
        else
            todoTxtCheck = false
        checkAddable()
    }

    private fun checkTodoDateMember() {
        if (!date_for_plan.isNullOrEmpty() && memberDTO != null && memberDTO.memberId != (-1).toLong()) {
            todoDateCheck = true
            todoMemberCheck = true
        } else {
            todoDateCheck = false
            todoMemberCheck = false
        }
        checkAddable()
    }

    private fun checkAddable() {
        if (todoTxtCheck && todoDateCheck && todoMemberCheck) {
            binding.btnRequestAddTodo.setImageDrawable(this.getDrawable(R.drawable.ic_send_enabled))
            binding.btnRequestAddTodo.setOnClickListener { onClickAddTodo() }
        } else {
            binding.btnRequestAddTodo.setImageDrawable(this.getDrawable(R.drawable.ic_send_disabled))
            binding.btnRequestAddTodo.setOnClickListener(null)
        }
    }

    fun onClickDateMember() {
        binding.textInputLayout.visibility = View.GONE

        val dateMemberDialog = DateMemberSetDialog(teamDTO.memberList, date_for_day)
        dateMemberDialog.setButtonClickListener(object : DateMemberSetDialog.OnButtonClickListener{
            override fun onCancelClicked() {
                dateMemberDialog.dismiss()
                binding.textInputLayout.visibility = View.VISIBLE
            }

            override fun onConfirmClicked() {
                date_for_plan = dateMemberDialog.selectedDate.toString()
                memberDTO = dateMemberDialog.getSelectMember()

                // 입력 레이아웃에 날짜, 담당자 반영
                binding.todoDate.text = dateMemberDialog.selectedDate.monthValue.toString() + "/" + dateMemberDialog.selectedDate.dayOfMonth.toString()
                binding.todoMemberName.text = memberDTO.nickName
                if (!memberDTO.profileImage.isNullOrEmpty()) {
                    binding.profileImg.visibility = View.VISIBLE
                    binding.defaultImage.visibility = View.GONE
                    Glide.with(binding.profileImg)
                        .load(memberDTO.profileImage)
                        .into(binding.profileImg)
                } else {
                    binding.profileImg.visibility = View.GONE
                    binding.defaultImage.visibility = View.VISIBLE
                    Glide.with(binding.profileImg)
                        .clear(binding.profileImg)
                }

                dateMemberDialog.dismiss()
                binding.textInputLayout.visibility = View.VISIBLE
                checkTodoDateMember()
            }
        })
        dateMemberDialog.show(supportFragmentManager, DateMemberSetDialog.TAG)
    }

    fun showBottomSheet(planId: Long) {
        val modalBottomSheet = ModalBottomSheetDialog(menuList)
        modalBottomSheet.setStyle(DialogFragment.STYLE_NORMAL, R.style.RoundCornerBottomSheetDialogTheme)
        modalBottomSheet.setDialogInterface(object : ModalBottomSheetDialog.ModalBottomSheetDialogInterface {
            override fun onFirstClickListener() {
                Toast.makeText(applicationContext, "아직 준비중이에요 !", Toast.LENGTH_SHORT).show()
            }

            override fun onSecondClickListener() {
                binding.viewModel!!.requestPlanDelete(prefs.getBearerToken(), teamDTO.teamId, planId)
                modalBottomSheet.dismiss()
            }

        })
        modalBottomSheet.show(supportFragmentManager, ModalBottomSheetDialog.TAG)
    }

    fun goToTeamMember() {
        val intent = Intent(this, TeamMemberActivity::class.java)
        intent.putExtra(Constants.INTENT_EXTRA_TEAM_DTO, teamDTO)
        startActivity(intent)
    }
}