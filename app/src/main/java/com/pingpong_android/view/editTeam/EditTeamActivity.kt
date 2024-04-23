package com.pingpong_android.view.editTeam

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.pingpong_android.R
import com.pingpong_android.base.BaseActivity
import com.pingpong_android.base.Constants
import com.pingpong_android.base.Constants.Companion.EDIT_TEAM
import com.pingpong_android.base.Constants.Companion.INTENT_EXTRA_MEMBER_LIST
import com.pingpong_android.base.Constants.Companion.INTENT_EXTRA_TEAM_DTO
import com.pingpong_android.base.Constants.Companion.INTENT_EXTRA_TEAM_NAME
import com.pingpong_android.databinding.ActivityEditTeamBinding
import com.pingpong_android.layout.HostDialog
import com.pingpong_android.layout.YNDialog
import com.pingpong_android.model.MemberDTO
import com.pingpong_android.model.TeamDTO
import com.pingpong_android.view.addMember.AddMemberActivity
import com.pingpong_android.view.myPage.MyPageActivity
import java.io.Serializable

class EditTeamActivity : BaseActivity<ActivityEditTeamBinding>(R.layout.activity_edit_team, TransitionMode.RIGHT) {

    lateinit var teamDTO: TeamDTO
    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private var memberAdapter = MemberAdapter(emptyList())
    private var memberList : MutableList<MemberDTO> = mutableListOf()
    private var newMemberList : MutableList<MemberDTO> = mutableListOf()
    private var goback : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = EditTeamViewModel()
        binding.activity = this

        initSubscribe()
        initView()
        onActivityResult()
    }

    private fun onActivityResult() {
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                if (it.data != null) {
                    var tmp = it.data!!.getSerializableExtra(Constants.INTENT_EXTRA_MEMBER_LIST) as MutableList<MemberDTO>
                    // 기존 팀원
                    memberList.clear()
                    memberList.addAll(teamDTO.memberList)
                    // 새로운 팀원
                    memberList.addAll(tmp)
                    memberAdapter.addList(memberList.toSet().toList())
                    newMemberList = tmp
                }
            }
        }
    }

    private fun initSubscribe() {
        subscribeDeleteTeam()
        subscribeEditTeam()
    }

    private fun initView() {
        nameEtEvent()
        binding.topPanel.setLeftClickListener(listener = { backDialog() })
        teamDTO = intent.getSerializableExtra(INTENT_EXTRA_TEAM_DTO) as TeamDTO
        memberList = teamDTO.memberList.toMutableList()

        // 팀 이름
        binding.groupNameEt.setText(teamDTO.teamName)

        // 팀 멤버
        val memberLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        memberAdapter.setHostId(teamDTO.hostId)
        memberAdapter.addList(teamDTO.memberList)
        binding.memberRv.apply {
            layoutManager = memberLayoutManager
            adapter = memberAdapter
        }
    }

    private fun subscribeDeleteTeam() {
        binding.viewModel!!.deleteTeamData.observe(this, Observer {
            if (it.isSuccess && it.type == "SUCCESS_DELETE_TEAM") {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                goToMyPage()
            } else {
                Toast.makeText(this, "팀 삭제를 다시 시도해주세요", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun subscribeEditTeam() {
        binding.viewModel!!.editTeamData.observe(this, Observer {
            if (it.isSuccess) {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                teamDTO.teamName = binding.groupNameEt.text.toString()
                requestInviteAlarm()

                // 이전 화면(팀 캘린더)에 변경 사항 전송
                intent = Intent()
                intent.putExtra(INTENT_EXTRA_TEAM_NAME, binding.groupNameEt.text.toString())
                setResult(RESULT_OK, intent)
                finish()
            } else {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun nameEtEvent() {
        binding.groupNameEt.addTextChangedListener(textWatcher)

        binding.groupNameEt.setOnEditorActionListener{ textView, action, event ->
            var handled = false
            if (action == EditorInfo.IME_ACTION_DONE) {
                // 키보드 내리기
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(window.decorView.applicationWindowToken, 0)

                handled = true
            }
            handled
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            if (!s.isNullOrEmpty())
                binding.viewModel!!.isReady.postValue(true)
            else
                binding.viewModel!!.isReady.postValue(false)
        }
    }

    private fun requestInviteAlarm() {
        if (newMemberList.size > 0) {
            for (member in newMemberList) {
                binding.viewModel!!.requestTeamInviteAlarm(prefs.getBearerToken(), teamDTO.teamId, member.memberId)
            }
        }
    }

    private fun backDialog() {
        val ynDialog = YNDialog(getString(R.string.stop_edit_team), listOf(getString(R.string.cancel), getString(R.string.out)))
        ynDialog.setButtonClickListener(object : YNDialog.OnButtonClickListener{
            override fun onFirstClicked() {
                // 취소
                goback = false
                ynDialog.dismiss()
            }

            override fun onSecondClicked() {
                // 나가기
                goback = true
                onBackPressed()
            }
        })
        ynDialog.show(supportFragmentManager, HostDialog.TAG)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (goback)
            super.onBackPressed()
        else
            backDialog()
    }

    fun editTeam() {
        binding.viewModel!!.requestEditTeam(prefs.getBearerToken(), teamDTO.teamId,
            binding.groupNameEt.text.toString(), getMemberId())
    }

    private fun getMemberId() : List<Long> {
        if (newMemberList.size > 0) {
            val memberIdList : MutableList<Long> = mutableListOf()

            for (memberDTO : MemberDTO in newMemberList)
                memberIdList.add(memberDTO.memberId)

            return memberIdList
        } else {
            return emptyList()
        }
    }

    fun deleteTeam() {
        val ynDialog = YNDialog(getString(R.string.delete_team_dialog), listOf(getString(R.string.cancel), getString(R.string.delete_word)))
        ynDialog.setButtonClickListener(object : YNDialog.OnButtonClickListener{
            override fun onFirstClicked() {
                // 취소
                ynDialog.dismiss()
            }

            override fun onSecondClicked() {
                // 삭제
                binding.viewModel!!.requestDeleteTeam(prefs.getBearerToken(), teamDTO.teamId)
                ynDialog.dismiss()
            }
        })
        ynDialog.show(supportFragmentManager, HostDialog.TAG)
    }

    fun goToFriendList() {
        val intent = Intent(this, AddMemberActivity::class.java)
        intent.putExtra(EDIT_TEAM, true)
        intent.putExtra(INTENT_EXTRA_MEMBER_LIST, teamDTO.memberList as Serializable)
        activityResultLauncher.launch(intent)
    }

    private fun goToMyPage() {
        val intent = Intent(this, MyPageActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }
}