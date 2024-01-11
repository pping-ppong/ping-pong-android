package com.pingpong_android.view.makeGroup

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.pingpong_android.R
import com.pingpong_android.base.BaseActivity
import com.pingpong_android.databinding.ActivityMakeGroupBinding
import com.pingpong_android.model.MemberDTO
import com.pingpong_android.utils.PreferenceUtil

class MakeGroupActivity : BaseActivity<ActivityMakeGroupBinding>(R.layout.activity_make_group) {

    private lateinit var prefs : PreferenceUtil
    private var memberAdapter = MemberAdapter(emptyList())
    private lateinit var memberList : MutableList<MemberDTO>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = MakeGroupViewModel()
        binding.activity = this

        prefs = PreferenceUtil(applicationContext)

        initAdapter()
    }

    private fun initAdapter() {
        val host = MemberDTO(prefs.getId().toLong())
        host.nickName = prefs.getNickName()
        host.profileImage = prefs.getProfileImg()
        memberList.add(0, host)

        val memberLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        memberAdapter.addList(memberList)

        binding.memberRv.apply {
            layoutManager = memberLayoutManager
            adapter = memberAdapter
        }
    }
}