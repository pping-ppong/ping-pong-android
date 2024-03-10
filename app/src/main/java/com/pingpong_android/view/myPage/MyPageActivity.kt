package com.pingpong_android.view.myPage

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.pingpong_android.R
import com.pingpong_android.base.BaseActivity
import com.pingpong_android.databinding.ActivityMyPageBinding
import com.pingpong_android.model.UserDTO
import com.pingpong_android.view.adapter.TeamAdapter
import com.pingpong_android.view.editProfile.EditProfileActivity
import com.pingpong_android.view.friends.FriendActivity
import com.pingpong_android.view.teamList.TeamListActivity
import com.pingpong_android.view.makeTeam.MakeTeamActivity
import com.pingpong_android.view.setting.SettingActivity

class MyPageActivity : BaseActivity<ActivityMyPageBinding>(R.layout.activity_my_page, TransitionMode.RIGHT) {

    private lateinit var user: UserDTO
    private var teamAdapter = TeamAdapter(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = MyPageViewModel()
        binding.activity = this

        user = prefs.getUser()
        friendView(user)

        initAdapter()
        initSubscribe()
    }

    override fun onResume() {
        super.onResume()
        requestMyPageInfo()
    }

    private fun requestMyPageInfo() {
        binding.viewModel!!.requestUserInfo(prefs.getBearerToken(), user)
        binding.viewModel!!.requestUserTeamList(prefs.getBearerToken())
    }

    private fun initSubscribe() {
        subscribeUserInfo()
        subscribeTeamListInfo()
        subscribeBadgeList()
    }

    private fun initAdapter() {
        val teamLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        teamAdapter.setContext(this)

        binding.teamRv.apply {
            layoutManager = teamLayoutManager
            adapter = teamAdapter
        }
    }

    private fun subscribeUserInfo() {
        binding.viewModel!!.UserData.observe(this, Observer {
            if (it.isSuccess) {
                // 유저 정보 조회 성공
                user.nickName = it.userDTO.nickName
                user.profileImage = it.userDTO.profileImage
                prefs.saveUser(user)

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
                binding.btnMore.visibility = View.VISIBLE

                // 보이는 그룹 개수 최대 2개
                if (it.teamList.size > 2)
                    teamAdapter.addList(it.teamList.subList(0, 2))
                else
                    teamAdapter.addList(it.teamList)
            } else {
                // 유저의 팀 조회 실패
                binding.teamRv.visibility = View.GONE
                binding.noTeamLayout.visibility = View.VISIBLE
                binding.btnMore.visibility = View.GONE
            }
        })
    }

    private fun subscribeBadgeList() {
        binding.viewModel!!.badgeList.observe(this, Observer {
            if (it.isSuccess && !it.badgeList.isEmpty()) {
                binding.noBadgeList.visibility = View.GONE
                binding.badgeRv.visibility = View.VISIBLE
                // todo
            } else {
                binding.noBadgeList.visibility = View.VISIBLE
                binding.badgeRv.visibility = View.GONE
                // todo
            }
        })
    }

    private fun friendView(user : UserDTO) {
        binding.topPanel.setTitle(user.nickName)
        binding.topPanel.setLeftClickListener(listener = { onBackPressed() })
        binding.topPanel.setRightClickListener(listener = { goToSetting() })

        binding.btnFriend.text = String.format(getString(R.string.friend_num), user.friendCnt)

        if (user.profileImage.isNotEmpty()) {
            binding.defaultImage.visibility = View.GONE
            Glide.with(binding.image)
                .load(user.profileImage)
                .into(binding.image)
            binding.image.clipToOutline = true
        } else {
            binding.defaultImage.visibility = View.VISIBLE
            Glide.with(binding.image).clear(binding.image)
        }
    }

    fun goToEditProfile() {
        val intent = Intent(this, EditProfileActivity::class.java)
        startActivity(intent)
    }

    fun goToSetting() {
        val intent = Intent(this, SettingActivity::class.java)
        startActivity(intent)
    }

    fun goToFriend() {
        val intent = Intent(this, FriendActivity::class.java)
        startActivity(intent)
    }

    fun goToMakeTeam() {
        val intent = Intent(this, MakeTeamActivity::class.java)
        startActivity(intent)
    }

    fun goToGroupList() {
        val intent = Intent(this, TeamListActivity::class.java)
        startActivity(intent)
    }
}