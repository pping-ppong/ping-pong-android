package com.pingpong_android.view.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.pingpong_android.R
import com.pingpong_android.base.BaseActivity
import com.pingpong_android.databinding.ActivityMainBinding
import com.pingpong_android.layout.MemberDialog
import com.pingpong_android.layout.ModalBottomSheetDialog
import com.pingpong_android.model.TeamDTO
import com.pingpong_android.model.UserDTO
import com.pingpong_android.view.main.adapter.CalendarAdapter
import com.pingpong_android.view.main.adapter.PlanTeamAdapter
import com.pingpong_android.view.makeTeam.MakeTeamActivity
import com.pingpong_android.view.myPage.MyPageActivity
import com.pingpong_android.view.notice.NoticeActivity
import com.pingpong_android.view.search.SearchActivity
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private lateinit var userDTO: UserDTO
    private var monthListAdapter = CalendarAdapter()
    private var todoListAdapter = PlanTeamAdapter(emptyList())

    private var date_for_cal: LocalDate = LocalDate.now().withDayOfMonth(1)
    private var date_for_day: LocalDate = LocalDate.now()

    private var menuList : List<String> = listOf("넘기기", "버리기")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = MainViewModel()
        binding.activity = this

        initAdapter()
        initUserDTO()
        initSubscribe()

        setClickListener()
    }

    override fun onResume() {
        super.onResume()
        initRequest()
    }

    // 유저 정보 바인딩
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

    // 유저 정보 다시 불러온 데이터로 리바인딩
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

    private fun initAdapter() {
        // 달력
        val monthListManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        monthListAdapter.setMainActivity(this)
        monthListAdapter.setDateToCalendar(date_for_cal)
        monthListAdapter.addAchieveList(emptyList())
        monthListAdapter.setPickedDate(date_for_day.dayOfMonth)

        binding.calender.apply {
            layoutManager = monthListManager
            adapter = monthListAdapter
            scrollToPosition(Int.MAX_VALUE/2)
        }

        // 할 일
        val planTeamListManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        todoListAdapter.setActivity(this)
        todoListAdapter.addTodoList(emptyList())

        binding.todoRv.apply {
            layoutManager = planTeamListManager
            adapter = todoListAdapter
        }
    }

    private fun setClickListener() {
        binding.btnMypage.setOnClickListener { goToMyPage() }
        binding.btnSearch.setOnClickListener { goToSearch()}
        binding.btnAlarm.setOnClickListener { goToNotice() }
    }

    // 데이터 요청
    private fun initRequest() {
        requestCalAchieveNow()
        binding.viewModel!!.requestPlans(prefs.getBearerToken(), date_for_day.toString())
        binding.viewModel!!.requestUserInfo(prefs.getBearerToken())
        binding.viewModel!!.requestUnReadNotice(prefs.getBearerToken())
        binding.viewModel!!.requestUserTeamList(prefs.getBearerToken())
    }

    // 달성률 조회
    private fun requestCalAchieveNow() {
        binding.viewModel!!.requestMonthAchievement(prefs.getBearerToken(),
                    startDate = date_for_cal.toString(),
                    endDate = date_for_cal.withDayOfMonth(date_for_cal.lengthOfMonth()).toString())
    }

    // 달성률 조회 (날짜 이동)
    fun requestCalAchieveNow(position : Int) {
        if (position == 1)
            date_for_cal = date_for_cal.plusMonths(1)
        else
            date_for_cal = date_for_cal.minusMonths(1)

        binding.viewModel!!.requestMonthAchievement(prefs.getBearerToken(),
            startDate = date_for_cal.toString(),
            endDate = date_for_cal.withDayOfMonth(date_for_cal.lengthOfMonth()).toString())
    }

    private fun initSubscribe() {
        subscribeTeamListInfo()
        subscribeAchieve()
        subscribeNoticeState()
        subscribeUserInfo()
        subscribePlans()
        subscribeComplete()
        subscribeDelete()
        subscribePass()
    }

    // 그룹 있는지 여부 확인
    private fun subscribeTeamListInfo() {
        binding.viewModel!!.teamListData.observe(this, Observer {
            if (it.isSuccess) {
                // 유저의 팀 조회 성공
                if (it.teamList.isNotEmpty()) {
                    binding.noTeamLayout.visibility = View.GONE
                    binding.todoRv.visibility = View.VISIBLE
                    // 그룹이 있는 경우 요청
                    binding.viewModel!!.requestPlans(prefs.getBearerToken(), date_for_day.toString())
                } else {
                    binding.noTeamLayout.visibility = View.VISIBLE
                    binding.todoRv.visibility = View.GONE
                }
            } else {
                // 유저의 팀 조회 실패
                binding.noTeamLayout.visibility = View.VISIBLE
                binding.todoRv.visibility = View.GONE
            }
        })
    }

    // 달성률 - request 결과
    private fun subscribeAchieve() {
        binding.viewModel!!.achieveResult.observe(this, Observer {
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
        binding.viewModel!!.plansResult.observe(this, Observer {
            if (it.isSuccess && !it.teamList.isNullOrEmpty()) {
                todoListAdapter.addTodoList(it.teamList)
                todoListAdapter.notifyDataSetChanged()
            } else {
                todoListAdapter.addTodoList(emptyList())
                todoListAdapter.notifyDataSetChanged()
            }
        })
    }

    // 할 일 완료/미완료 - request 결과
    private fun subscribeComplete() {
        binding.viewModel!!.planRequestResult.observe(this, Observer {
            if (it.isSuccess) {
                requestCalAchieveNow()
                binding.viewModel!!.requestPlans(prefs.getBearerToken(), date_for_day.toString())
            }
        })
    }

    private fun subscribeDelete() {
        binding.viewModel!!.deleteResult.observe(this, Observer {
            if (it.isSuccess) {
                requestCalAchieveNow()
                binding.viewModel!!.requestPlans(prefs.getBearerToken(), date_for_day.toString())
            }
        })
    }

    private fun subscribePass() {
        binding.viewModel!!.passResult.observe(this, Observer {
            if (it.isSuccess) {
                requestCalAchieveNow()
                binding.viewModel!!.requestPlans(prefs.getBearerToken(), date_for_day.toString())
            }
        })
    }

    // 알림 - request 결과
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun subscribeNoticeState() {
        binding.viewModel!!.noticeState.observe(this, Observer {
            if (it.isSuccess) {
                if (it.notificationExists)
                    binding.btnAlarm.setImageDrawable(getDrawable(R.drawable.ic_alarm_on))
                else if (!it.notificationExists)
                    binding.btnAlarm.setImageDrawable(getDrawable(R.drawable.ic_alarm_setting))
                else
                    binding.btnAlarm.setImageDrawable(getDrawable(R.drawable.ic_alarm_off))
            } else {
                binding.btnAlarm.setImageDrawable(getDrawable(R.drawable.ic_alarm_off))
            }
        })
    }

    // 유저 정보 - request 결과
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

    // 할 일 불러오기
    fun requestPlans(day : Date) {
        date_for_day = day.toInstant()
                          .atZone(ZoneId.systemDefault())
                          .toLocalDate()
        binding.viewModel!!.requestPlans(prefs.getBearerToken(), date_for_day.toString())
        monthListAdapter.setPickedDate(date_for_day.dayOfMonth)
    }

    // 할 일 완료/미완료 처리
    fun requestPlanComplete(isComplete : Boolean, teamId: Long, planId: Long) {
        if (isComplete) {   // 완료 표시
            binding.viewModel!!.requestPlanComplete(prefs.getBearerToken(), teamId, planId)
        } else {    // 미완료 표시
            binding.viewModel!!.requestPlanIncomplete(prefs.getBearerToken(), teamId, planId)
        }
    }

    fun showBottomSheet(team: TeamDTO, planId: Long) {
        val modalBottomSheet = ModalBottomSheetDialog(menuList)
        modalBottomSheet.setStyle(DialogFragment.STYLE_NORMAL, R.style.RoundCornerBottomSheetDialogTheme)
        modalBottomSheet.setDialogInterface(object : ModalBottomSheetDialog.ModalBottomSheetDialogInterface {
            override fun onFirstClickListener() {
                // 넘기기
                showMemberDialog(team, planId)
                modalBottomSheet.dismiss()
            }

            override fun onSecondClickListener() {
                // 버리기
                binding.viewModel!!.requestPlanDelete(prefs.getBearerToken(), team.teamId, planId)
                modalBottomSheet.dismiss()
            }

        })
        modalBottomSheet.show(supportFragmentManager, ModalBottomSheetDialog.TAG)
    }

    fun showMemberDialog(team: TeamDTO, planId: Long) {
        var tmp = binding.viewModel!!.teamListData.value!!.teamList.toMutableList()
        var memberList = tmp.get(tmp.indexOf(team)).memberList

        val memberDialog = MemberDialog(memberList)
        memberDialog.setButtonClickListener(object : MemberDialog.OnButtonClickListener{
            override fun onCancelClicked() {
                memberDialog.dismiss()
            }

            override fun onConfirmClicked() {
                var memberId = memberDialog.getSelectMember()
                binding.viewModel!!.requestPassPlan(prefs.getBearerToken(), team.teamId, planId, memberId)
                binding.viewModel!!.requestPassAlarm(prefs.getBearerToken(), planId, memberId)
                memberDialog.dismiss()
            }

        })
        memberDialog.show(supportFragmentManager, MemberDialog.TAG)
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

    fun goToMakeTeam() {
        val intent = Intent(this, MakeTeamActivity::class.java)
        startActivity(intent)
    }
}