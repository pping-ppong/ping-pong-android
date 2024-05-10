package com.pingpong_android.view.makeTeam

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
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
import com.pingpong_android.base.Constants.Companion.INTENT_EXTRA_MEMBER_LIST
import com.pingpong_android.databinding.ActivityMakeTeamBinding
import com.pingpong_android.layout.HostDialog
import com.pingpong_android.layout.YNDialog
import com.pingpong_android.model.MemberDTO
import com.pingpong_android.model.TeamDTO
import com.pingpong_android.view.addMember.AddMemberActivity
import java.io.Serializable
import java.util.regex.Pattern

class MakeTeamActivity : BaseActivity<ActivityMakeTeamBinding>(R.layout.activity_make_team, TransitionMode.RIGHT) {

    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private var memberAdapter = MemberAdapter(emptyList())
    private var memberList : MutableList<MemberDTO> = mutableListOf()
    private var goBack : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = MakeTeamViewModel()
        binding.activity = this

        subscribeTeamData()
        initAdapter()
        nameEtEvent()

        onActivityResult()
        binding.topPanel.setLeftClickListener(listener = { onBackPressed() })
    }

    override fun onBackPressed() {
        if (binding.viewModel!!.isReady.value!!) {
            backDialog()
        } else if (goBack) {
            super.onBackPressed()
        } else
            super.onBackPressed()
    }

    private fun backDialog() {
        val ynDialog = YNDialog(getString(R.string.stop_edit_team), listOf(getString(R.string.cancel), getString(R.string.out)))
        ynDialog.setButtonClickListener(object : YNDialog.OnButtonClickListener{
            override fun onFirstClicked() {
                // 취소
                goBack = false
                ynDialog.dismiss()
            }

            override fun onSecondClicked() {
                // 나가기
                goBack = true
                onBackPressed()
            }
        })
        ynDialog.show(supportFragmentManager, HostDialog.TAG)
    }

    private fun onActivityResult() {
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                if (it.data != null) {
                    memberList.clear()
                    val host = MemberDTO(prefs.getId().toLong())
                    host.nickName = prefs.getNickName()
                    host.profileImage = prefs.getProfileImg()
                    memberList.add(host)

                    memberList.addAll(it.data!!.getSerializableExtra(INTENT_EXTRA_MEMBER_LIST) as MutableList<MemberDTO>)
                    memberAdapter.addList(memberList.toSet().toList())
                }
            }
        }
    }

    private fun initAdapter() {
        val host = MemberDTO(prefs.getId().toLong())
        host.nickName = prefs.getNickName()
        host.profileImage = prefs.getProfileImg()
        memberList.add(host)

        val memberLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        memberAdapter.addList(memberList.toList())

        binding.memberRv.apply {
            layoutManager = memberLayoutManager
            adapter = memberAdapter
        }
    }

    private fun nameEtEvent() {
        binding.groupNameEt.addTextChangedListener(textWatcher)
        binding.groupNameEt.filters = arrayOf(filterAlphaNumSpaceSpecialChar)

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
            if (!s.isNullOrEmpty() && checkLength(s)) {
                    binding.viewModel!!.isReady.postValue(true)
            } else {
                binding.viewModel!!.isReady.postValue(false)
            }
        }
    }

    var filterAlphaNumSpaceSpecialChar = InputFilter { source, start, end, dest, dstart, dend ->
        /*
            [요약 설명]
            1. 정규식 패턴 ^[a-z] : 영어 소문자 허용
            2. 정규식 패턴 ^[A-Z] : 영어 대문자 허용
            3. 정규식 패턴 ^[ㄱ-ㅣ가-힣] : 한글 허용
            4. 정규식 패턴 ^[0-9] : 숫자 허용
            5. 정규식 패턴 ^[ ] or ^[\\s] : 공백 허용
        */
        val ps = Pattern.compile("^[ㄱ-ㅣ가-힣a-zA-Z0-9\\s!@#$%^&*()\\-+]+$")
        if (!ps.matcher(source).matches()) {
            ""
        } else source
    }

    private fun checkLength(s: CharSequence) : Boolean {
        if (s.length <= 10) {
            binding.groupNameETLayout.error = null
            return true
        } else {
            binding.groupNameETLayout.error = "10자를 초과할 수 없습니다."
            return false
        }
    }

    private fun subscribeTeamData() {
        binding.viewModel!!.teamData.observe(this, Observer {
            if (it.isSuccess) {
                requestInviteAlarm(it.team)
                finish()
            } else
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
        })
    }

    private fun requestInviteAlarm(team : TeamDTO) {
        val tmp = team.memberIdList.toMutableList()
        tmp.remove(prefs.getId().toLong())
        for (id in tmp) {
            binding.viewModel!!.requestTeamInviteAlarm(prefs.getBearerToken(), team.teamId, id)
        }
    }

    fun makeGroup() {
        binding.viewModel!!.requestMakeGroup(prefs.getBearerToken(), binding.groupNameEt.text.toString(), memberAdapter.
        getMemberId())
    }

    fun goToFriendList() {
        val intent = Intent(this, AddMemberActivity::class.java)
        intent.putExtra(INTENT_EXTRA_MEMBER_LIST, memberList as Serializable)
        activityResultLauncher.launch(intent)
    }
}