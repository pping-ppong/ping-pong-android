package com.pingpong_android.view.teamList

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.pingpong_android.R
import com.pingpong_android.base.BaseActivity
import com.pingpong_android.databinding.ActivityTeamListBinding
import com.pingpong_android.layout.ModalBottomSheetDialog
import com.pingpong_android.view.adapter.TeamAdapter
import com.pingpong_android.view.makeTeam.MakeTeamActivity

class TeamListActivity : BaseActivity<ActivityTeamListBinding>(R.layout.activity_team_list, TransitionMode.RIGHT) {

    private var teamAdapter = TeamAdapter(emptyList())
    private var menuList : List<String> = listOf("그룹 만들기", "그룹 순서 편집")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = TeamListViewModel()
        binding.activity = this

        initAdapter()
        subscribeTeamListInfo()
        setClickListener()

        binding.viewModel!!.requestUserTeamList(prefs.getBearerToken())
    }

    private fun setClickListener() {
        binding.topPanel.setLeftClickListener(listener = {onBackPressed()})
        binding.topPanel.setRightClickListener(listener = {showBottomSheet()})
    }

    private fun initAdapter() {
        val teamLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        teamAdapter.setContext(this)

        binding.teamRv.apply {
            layoutManager = teamLayoutManager
            adapter = teamAdapter
        }
    }

    private fun subscribeTeamListInfo() {
        binding.viewModel!!.teamList.observe(this, Observer {
            if (it.isSuccess && it.teamList.isNotEmpty()) {
                // 유저의 팀 조회 성공
                binding.teamRv.visibility = View.VISIBLE

                // 보이는 그룹 개수 최대 2개
                if (it.teamList.size > 2)
                    teamAdapter.addList(it.teamList.subList(0, 2))
                else
                    teamAdapter.addList(it.teamList)
            } else {
                // 유저의 팀 조회 실패
                binding.teamRv.visibility = View.GONE
            }
        })
    }

    private fun showBottomSheet() {
        val modalBottomSheet = ModalBottomSheetDialog(menuList)
        modalBottomSheet.setStyle(DialogFragment.STYLE_NORMAL, R.style.RoundCornerBottomSheetDialogTheme)
        modalBottomSheet.setDialogInterface(object : ModalBottomSheetDialog.ModalBottomSheetDialogInterface {
            override fun onFirstClickListener() {
                goToMakeTeam()
                modalBottomSheet.dismiss()
            }

            override fun onSecondClickListener() {
                Toast.makeText(applicationContext, "아직 준비중이에요 !", Toast.LENGTH_SHORT).show()
            }

        })
        modalBottomSheet.show(supportFragmentManager, ModalBottomSheetDialog.TAG)
    }

    fun goToMakeTeam() {
        val intent = Intent(this, MakeTeamActivity::class.java)
        startActivity(intent)
    }
}