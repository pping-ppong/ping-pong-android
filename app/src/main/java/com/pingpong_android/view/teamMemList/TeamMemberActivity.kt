package com.pingpong_android.view.teamMemList

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.pingpong_android.R
import com.pingpong_android.base.BaseActivity
import com.pingpong_android.base.Constants
import com.pingpong_android.databinding.ActivityTeamMemberBinding
import com.pingpong_android.layout.HostDialog
import com.pingpong_android.model.MemberDTO
import com.pingpong_android.model.TeamDTO
import com.pingpong_android.view.profile.ProfileActivity

class TeamMemberActivity : BaseActivity<ActivityTeamMemberBinding>(R.layout.activity_team_member, TransitionMode.RIGHT) {

    private lateinit var teamDTO: TeamDTO
    private var teamMemberAdapter = TeamMemberAdapter(emptyList())
    private var menuList : List<String> = listOf("방장 넘기기", "내보내기")

    private var memberId_apply_friendship : Long = -1
    var isHost : Boolean = false
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
        isHost = prefs.getId() == teamDTO.hostId.toString()
        binding.topPanel.setLeftClickListener(listener = { onBackPressed() })

        // adapter
        val teamLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        teamMemberAdapter.setTeamMemberActivity(this)
        binding.memberRv.apply {
            layoutManager = teamLayoutManager
            adapter = teamMemberAdapter
        }
    }

    private fun initSubscribe() {
        subscribeAllTeamMember()
        subscribeApplyFriendShip()
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
                binding.viewModel!!.requestAlarmFriend(prefs.getBearerToken(), memberId_apply_friendship)
                memberId_apply_friendship = -1
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            } else {
                memberId_apply_friendship = -1
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    fun requestFriendship(memberId: Long) {
        binding.viewModel!!.requestFriendShip(prefs.getBearerToken(), prefs.getId().toLong(), memberId)
        memberId_apply_friendship = memberId
    }

    fun onClickHostDialog(member : MemberDTO) {
        val hostDialog = HostDialog(menuList, member)
        hostDialog.setButtonClickListener(object : HostDialog.OnButtonClickListener{
            override fun onFirstClicked() {
                Toast.makeText(this@TeamMemberActivity, "방장 넘기기", Toast.LENGTH_SHORT).show()
            }

            override fun onSecondClicked() {
                Toast.makeText(this@TeamMemberActivity, "내보내기", Toast.LENGTH_SHORT).show()
            }
        })
        hostDialog.show(supportFragmentManager, HostDialog.TAG)
    }

    fun goToUserProfile(memberId : Long) {
        val intent = Intent(this, ProfileActivity::class.java)
        intent.putExtra(Constants.INTENT_EXTRA_MEMBER_ID, memberId)
        startActivity(intent)
    }
}