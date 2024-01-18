package com.pingpong_android.view.addMember

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.pingpong_android.R
import com.pingpong_android.base.BaseActivity
import com.pingpong_android.base.Constants.Companion.INTENT_EXTRA_MEMBER_LIST
import com.pingpong_android.databinding.ActivityAddMemberBinding
import com.pingpong_android.model.MemberDTO
import com.pingpong_android.view.makeGroup.MakeGroupActivity
import java.io.Serializable

class AddMemberActivity : BaseActivity<ActivityAddMemberBinding>(R.layout.activity_add_member) {

    private var addMemberAdapter = AddMemberAdapter(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = AddMemberViewModel()
        binding.activity = this

        searchEvent()
        subscribeFriendList()

        initAdapter()
        initView()

        binding.viewModel!!.requestUserFriendList(prefs.getBearerToken())
    }

    private fun initAdapter() {
        val friendLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        addMemberAdapter.setActivity(this)

        binding.memberRv.apply {
            layoutManager = friendLayoutManager
            adapter = addMemberAdapter
        }
    }
    private fun initView() {
        binding.topPanel.setLeftClickListener(listener = {onBackPressed()})
        binding.topPanel.setRightClickListener(listener = {sendSelectedMembers()})


    }

    private fun subscribeFriendList() {
        binding.viewModel!!.friendList.observe(this, Observer {
            if(it.isSuccess && it.friendList.isNotEmpty()) {
                binding.noFriendList.visibility = View.GONE
                binding.memberRv.visibility = View.VISIBLE
                addMemberAdapter.addList(it.friendList)
            } else {
                binding.noFriendList.visibility = View.VISIBLE
                binding.memberRv.visibility = View.GONE
                addMemberAdapter.addList(emptyList())
            }
        })
    }


    private fun searchEvent() {
        binding.nickNmEt.setOnEditorActionListener{ textView, action, event ->
            var handled = false
            if (action == EditorInfo.IME_ACTION_DONE) {
                searchKeyword(binding.nickNmEt.text.toString())

                // 키보드 내리기
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(window.decorView.applicationWindowToken, 0)

                handled = true
            }
            handled
        }
    }

    fun searchKeyword(keyword : String) {
        // todo : list 중에서 동일한 이름 뽑기
    }

     private fun sendSelectedMembers() {
        val intent = Intent(this, MakeGroupActivity::class.java)
        intent.putExtra(INTENT_EXTRA_MEMBER_LIST, addMemberAdapter.getSelectedList() as Serializable)
        setResult(RESULT_OK, intent)
        if (!isFinishing) finish()
    }
}