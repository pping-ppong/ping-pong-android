package com.pingpong_android.view.makeGroup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.pingpong_android.R
import com.pingpong_android.base.BaseActivity
import com.pingpong_android.databinding.ActivityMakeGroupBinding
import com.pingpong_android.model.MemberDTO
import com.pingpong_android.utils.PreferenceUtil
import com.pingpong_android.view.friends.FriendActivity
import com.pingpong_android.view.gallery.GalleryActivity

class MakeGroupActivity : BaseActivity<ActivityMakeGroupBinding>(R.layout.activity_make_group, TransitionMode.RIGHT) {

    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private var memberAdapter = MemberAdapter(emptyList())
    private lateinit var memberList : MutableList<MemberDTO>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = MakeGroupViewModel()
        binding.activity = this

        initAdapter()
        nameEtEvent()

        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
//                val return = it.data?.getStringExtra("return") ?: ""
            }
        }
    }

    private fun initAdapter() {
        val host = MemberDTO(prefs.getId().toLong())
        host.nickName = prefs.getNickName()
        host.profileImage = prefs.getProfileImg()
        memberList = mutableListOf(host)

        val memberLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        memberAdapter.addList(memberList)

        binding.memberRv.apply {
            layoutManager = memberLayoutManager
            adapter = memberAdapter
        }
    }

    private fun nameEtEvent() {
        binding.groupNameEt.addTextChangedListener(textWatcher)

        binding.groupNameEt.setOnEditorActionListener{ textView, action, event ->
            var handled = false
            if (action == EditorInfo.IME_ACTION_DONE) {
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
            if (!s.isNullOrEmpty())
                binding.viewModel!!.isReady.postValue(true)
            else
                binding.viewModel!!.isReady.postValue(false)
        }
    }

    fun makeGroup() {
        binding.viewModel!!.requestMakeGroup(prefs.getBearerToken(), binding.groupNameEt.text.toString(), memberAdapter.getMemberId())
    }

    fun goToFriendList() {
        val intent = Intent(this, FriendActivity::class.java)
        activityResultLauncher.launch(intent)
    }
}