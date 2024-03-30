package com.pingpong_android.view.teamMemList

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.pingpong_android.R
import com.pingpong_android.base.BaseActivity
import com.pingpong_android.base.Constants
import com.pingpong_android.base.Constants.Companion.INTENT_EXTRA_MEMBER_LIST
import com.pingpong_android.databinding.ActivityTeamMemberBinding
import com.pingpong_android.layout.HostDialog
import com.pingpong_android.layout.YNDialog
import com.pingpong_android.model.MemberDTO
import com.pingpong_android.model.TeamDTO
import com.pingpong_android.view.profile.ProfileActivity
import java.io.Serializable

class TeamMemberActivity : BaseActivity<ActivityTeamMemberBinding>(R.layout.activity_team_member, TransitionMode.RIGHT) {

    private lateinit var teamDTO: TeamDTO
    private var teamMemberAdapter = TeamMemberAdapter(emptyList())
    private var menuList : List<String> = listOf("방장 넘기기", "내보내기")
    var myId : Long = prefs.getId().toLong()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = TeamMemberViewModel()
        binding.activity = this

        initView()
        initSubscribe()

        binding.viewModel!!.requestTeamMemberList(prefs.getBearerToken(), teamDTO.teamId)
    }

    private fun initView() {
        teamDTO = intent.getSerializableExtra(Constants.INTENT_EXTRA_TEAM_DTO) as TeamDTO
        binding.topPanel.setLeftClickListener(listener = { onBackPressed() })

        // adapter
        val teamLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        teamMemberAdapter.setTeamMemberActivity(this)
        binding.memberRv.apply {
            layoutManager = teamLayoutManager
            adapter = teamMemberAdapter
        }
    }

    override fun onBackPressed() {
        val memberList = binding.viewModel!!.teamMemberResult.value!!.friendList
        intent = Intent()
        intent.putExtra(INTENT_EXTRA_MEMBER_LIST,memberList as Serializable)
        setResult(RESULT_OK, intent)
        finish()

        super.onBackPressed()
    }

    private fun initSubscribe() {
        subscribeAllTeamMember()
        subscribeApplyFriendShip()
        subscribeEmitMember()
        subscribeChangeHost()
    }

    private fun subscribeAllTeamMember() {
        binding.viewModel!!.teamMemberResult.observe(this, Observer {
            if (it.isSuccess && it.friendList.isNotEmpty()) {
                teamMemberAdapter.addList(it.friendList)
            } else {
                teamMemberAdapter.addList(emptyList())
            }
        })
    }

    private fun subscribeApplyFriendShip() {
        binding.viewModel!!.applyResult.observe(this, Observer {
            if (it.isSuccess && it.code == 200) {
                binding.viewModel!!.requestTeamMemberList(prefs.getBearerToken(), teamDTO.teamId)
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun subscribeEmitMember() {
        binding.viewModel!!.emitResult.observe(this, Observer {
            binding.viewModel!!.requestTeamMemberList(prefs.getBearerToken(), teamDTO.teamId)
        })
    }

    private fun subscribeChangeHost() {
        binding.viewModel!!.changeHostResult.observe(this, Observer {
            binding.viewModel!!.requestTeamMemberList(prefs.getBearerToken(), teamDTO.teamId)
        })
    }

    fun requestFriendship(memberId: Long) {
        binding.viewModel!!.requestFriendShip(prefs.getBearerToken(), prefs.getId().toLong(), memberId)
        binding.viewModel!!.requestAlarmFriend(prefs.getBearerToken(), memberId)
    }

    fun onClickHostDialog(member : MemberDTO) {
        val hostDialog = HostDialog(menuList, member)
        hostDialog.setButtonClickListener(object : HostDialog.OnButtonClickListener{
            override fun onFirstClicked() {
                showChangeHostDialog(member)
                hostDialog.dismiss()
            }

            override fun onSecondClicked() {
                showEmitMemberDialog(member)
                hostDialog.dismiss()
            }
        })
        hostDialog.show(supportFragmentManager, HostDialog.TAG)
    }

    // 팀원 방출하기
    fun showEmitMemberDialog(member: MemberDTO) {
        val ynDialog = YNDialog(String.format(getString(R.string.emit_member), member.nickName),
            listOf(getString(R.string.cancel), getString(R.string.get_rid_of)))
        ynDialog.setButtonClickListener(object : YNDialog.OnButtonClickListener{
            override fun onFirstClicked() {
                // 취소
                ynDialog.dismiss()
            }

            override fun onSecondClicked() {
                // 내보내기
                binding.viewModel!!.requestEmitMember(prefs.getBearerToken(), teamDTO.teamId, member.memberId)
                ynDialog.dismiss()
            }
        })
        ynDialog.show(supportFragmentManager, HostDialog.TAG)
    }

    // 방장 위임하기
    fun showChangeHostDialog(member: MemberDTO) {
        val ynDialog = YNDialog(String.format(getString(R.string.change_member), member.nickName),
            listOf(getString(R.string.cancel), getString(R.string.confirm)))
        ynDialog.setButtonClickListener(object : YNDialog.OnButtonClickListener{
            override fun onFirstClicked() {
                // 취소
                ynDialog.dismiss()
            }

            override fun onSecondClicked() {
                // 확인
                binding.viewModel!!.requestChangeHost(prefs.getBearerToken(), teamDTO.teamId, member.memberId)
                ynDialog.dismiss()
            }
        })
        ynDialog.show(supportFragmentManager, HostDialog.TAG)
    }

    fun goToUserProfile(memberId : Long) {
        val intent = Intent(this, ProfileActivity::class.java)
        intent.putExtra(Constants.INTENT_EXTRA_MEMBER_ID, memberId)
        startActivity(intent)
    }
}