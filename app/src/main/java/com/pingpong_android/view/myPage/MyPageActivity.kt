package com.pingpong_android.view.myPage

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.pingpong_android.R
import com.pingpong_android.base.BaseActivity
import com.pingpong_android.databinding.ActivityMyPageBinding
import com.pingpong_android.model.UserDTO
import com.pingpong_android.utils.PreferenceUtil
import com.pingpong_android.view.editProfile.EditProfileActivity
import com.pingpong_android.view.friends.FriendActivity

class MyPageActivity : BaseActivity<ActivityMyPageBinding>(R.layout.activity_my_page) {

    private lateinit var prefsUtil : PreferenceUtil
    private lateinit var user: UserDTO
    private var teamAdapter = TeamAdapter(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = MyPageViewModel()
        binding.activity = this

        prefsUtil = PreferenceUtil(applicationContext)
        user = prefsUtil.getUser()

        initAdapter()
        initSubscribe()
        requestMyPageInfo()
    }

    private fun requestMyPageInfo() {
        binding.viewModel!!.requestUserInfo(prefsUtil.getBearerToken(), user)
        binding.viewModel!!.requestUserTeamList(prefsUtil.getBearerToken())
    }

    private fun initSubscribe() {
        subscribeUserInfo()
        subscribeTeamListInfo()
    }

    private fun initAdapter() {
        val teamLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        teamAdapter.setActivity(this)

        binding.teamRv.apply {
            layoutManager = teamLayoutManager
            adapter = teamAdapter
        }
    }

    private fun subscribeUserInfo() {
        binding.viewModel!!.UserData.observe(this, Observer {
            if (it.isSuccess) {
                // 유저 정보 조회 성공
                friendView(it.userDTO)
            } else {
                // 유저 정보 조회 실패
                friendView(user)
            }
        })
    }

    private fun subscribeTeamListInfo() {
        binding.viewModel!!.TeamList.observe(this, Observer {
            if (it.isSuccess && it.teamList.isNotEmpty()) {
                // 유저의 팀 조회 성공
                binding.teamRv.visibility = View.VISIBLE
                binding.noTeamLayout.visibility = View.GONE

                teamAdapter.addList(it.teamList.subList(0,2))
            } else {
                // 유저의 팀 조회 실패
                binding.teamRv.visibility = View.GONE
                binding.noTeamLayout.visibility = View.VISIBLE
            }
        })
    }

    private fun friendView(user : UserDTO) {
        binding.userNm.text = user.nickName
        binding.btnFriend.text = String.format(getString(R.string.friend_num), user.friendCnt)
    }

    fun goToEditProfile() {
        val intent = Intent(this, EditProfileActivity::class.java)
        startActivity(intent)
    }

    fun goToFriend() {
        val intent = Intent(this, FriendActivity::class.java)
        startActivity(intent)
    }

    fun goToMakeTeam() {
        // todo : 팀 그룹 만들기 페이지로 이동
    }
}