package com.pingpong_android.view.teamMemList

import android.os.Bundle
import com.pingpong_android.R
import com.pingpong_android.base.BaseActivity
import com.pingpong_android.databinding.ActivityTeamMemberBinding

class TeamMemberActivity : BaseActivity<ActivityTeamMemberBinding>(R.layout.activity_team_member, TransitionMode.RIGHT) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = TeamMemberViewModel()
        binding.activity = this


    }
}