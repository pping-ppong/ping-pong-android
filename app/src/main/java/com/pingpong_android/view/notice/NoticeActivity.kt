package com.pingpong_android.view.notice

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.pingpong_android.R
import com.pingpong_android.base.BaseActivity
import com.pingpong_android.databinding.ActivityNoticeBinding

class NoticeActivity : BaseActivity<ActivityNoticeBinding>(R.layout.activity_notice) {

    private var noticeAdapter = NoticeAdapter(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = NoticeViewModel()
        binding.activity = this

        initAdapter()
        subscribeNotice()
        subscribeResult()

        binding.topPanel.setLeftClickListener(listener = {onBackPressed()})
        binding.viewModel!!.requestAllNotice(prefs.getBearerToken())
    }

    private fun initAdapter() {
        val noticeLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        noticeAdapter.setActivity(this)

        binding.noticeRv.apply {
            layoutManager = noticeLayoutManager
            adapter = noticeAdapter
        }
    }

    private fun subscribeNotice() {
        binding.viewModel!!.noticeList.observe(this, Observer {
            if (it.isSuccess && it.noticeList.isNotEmpty()) {
                binding.noticeRv.visibility = View.VISIBLE
                binding.noNoticeList.visibility = View.GONE

                noticeAdapter.addList(it.noticeList)
            } else {
                binding.noticeRv.visibility = View.GONE
                binding.noNoticeList.visibility = View.VISIBLE

                noticeAdapter.addList(emptyList())
            }
        })
    }

    private fun subscribeResult() {
        binding.viewModel!!.result.observe(this, Observer {
            if (it.isSuccess) {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                binding.viewModel!!.requestAllNotice(prefs.getBearerToken())
            }
        })
    }

    fun acceptFriendShip(respondentId : Long) {
        binding.viewModel!!.acceptFriendShip(prefs.getBearerToken(), respondentId)
    }

    fun refuseFriendShip(respondentId : Long) {
        binding.viewModel!!.refuseFriendShip(prefs.getBearerToken(), respondentId)
    }
}