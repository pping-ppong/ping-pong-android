package com.pingpong_android.view.addMember

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import com.pingpong_android.R
import com.pingpong_android.base.BaseActivity
import com.pingpong_android.databinding.ActivityAddMemberBinding

class AddMemberActivity : BaseActivity<ActivityAddMemberBinding>(R.layout.activity_add_member) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = AddMemberViewModel()
        binding.activity = this

        searchEvent()
        initView()
    }

    private fun initView() {
        binding.topPanel.setLeftClickListener(listener = {onBackPressed()})
//        binding.topPanel.setRightClickListener(listener = {})

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
}