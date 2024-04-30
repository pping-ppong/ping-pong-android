package com.pingpong_android.view.addMember

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.pingpong_android.R
import com.pingpong_android.base.BaseActivity
import com.pingpong_android.base.Constants
import com.pingpong_android.base.Constants.Companion.INTENT_EXTRA_MEMBER_LIST
import com.pingpong_android.databinding.ActivityAddMemberBinding
import com.pingpong_android.model.MemberDTO
import com.pingpong_android.view.editTeam.EditTeamActivity
import com.pingpong_android.view.makeTeam.MakeTeamActivity
import java.io.Serializable
import kotlin.streams.toList

class AddMemberActivity : BaseActivity<ActivityAddMemberBinding>(R.layout.activity_add_member) {

    private var addMemberAdapter = AddMemberAdapter(emptyList())
    private lateinit var friendList: List<MemberDTO>

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

        // 팀 수정에서 넘어온 경우
        if (intent.getBooleanExtra(Constants.EDIT_TEAM, false)) {
            addMemberAdapter.addMemberList(intent.getSerializableExtra(Constants.INTENT_EXTRA_MEMBER_LIST) as List<MemberDTO>)
        }

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
                friendList = it.friendList
            } else {
                binding.noFriendList.visibility = View.VISIBLE
                binding.memberRv.visibility = View.GONE
                addMemberAdapter.addList(emptyList())
            }
        })
    }


    private fun searchEvent() {
        binding.nickNmEt.addTextChangedListener(textWatcher)
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

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            if (s.isNullOrEmpty())
                addMemberAdapter.addList(friendList)
        }
    }

    fun searchKeyword(keyword : String) {
        addMemberAdapter.addList(friendList.stream().filter { it.nickName.contains(keyword) }.toList())
    }

     private fun sendSelectedMembers() {
         // 팀 수정에서 넘어온 경우
         if (intent.getBooleanExtra(Constants.EDIT_TEAM, false)) {
             val intent = Intent(this, EditTeamActivity::class.java)
             intent.putExtra(INTENT_EXTRA_MEMBER_LIST, addMemberAdapter.getSelectedList() as Serializable)
             setResult(RESULT_OK, intent)
             if (!isFinishing) finish()
         } else {
             val intent = Intent(this, MakeTeamActivity::class.java)
             intent.putExtra(INTENT_EXTRA_MEMBER_LIST, addMemberAdapter.getSelectedList() as Serializable)
             setResult(RESULT_OK, intent)
             if (!isFinishing) finish()
         }
    }
}