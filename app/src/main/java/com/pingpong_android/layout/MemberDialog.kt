package com.pingpong_android.layout

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.pingpong_android.databinding.LayoutDateMemberBinding
import com.pingpong_android.databinding.LayoutMemberDialogBinding
import com.pingpong_android.model.MemberDTO
import com.pingpong_android.view.adapter.MemberHorizontalAdapter

class MemberDialog(val memberList : List<MemberDTO>) : DialogFragment() {

    private lateinit var binding : LayoutMemberDialogBinding
    private lateinit var buttonClickListener : OnButtonClickListener
    val memberAdapter = MemberHorizontalAdapter(memberList, true)

    companion object {
        const val TAG = "MemberDialog"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LayoutMemberDialogBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        return binding.root
    }

    interface OnButtonClickListener{
        fun onCancelClicked()
        fun onConfirmClicked()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 버튼
        binding.btnNo.setOnClickListener { buttonClickListener.onCancelClicked() }
        binding.btnYes.setOnClickListener { buttonClickListener.onConfirmClicked() }

        val memberLayoutManager = FlexboxLayoutManager(getContext())
        memberLayoutManager.flexDirection = FlexDirection.ROW
        memberLayoutManager.justifyContent = JustifyContent.FLEX_START

        binding.memberRv.apply {
            layoutManager = memberLayoutManager
            adapter = memberAdapter
        }
    }

    fun getSelectMember() : Long {
        return memberAdapter.getSelectedMember().memberId
    }

    fun setButtonClickListener(buttonClickListener: OnButtonClickListener) {
        this.buttonClickListener = buttonClickListener
    }

    override fun onStart() {
        super.onStart();
        val lp : WindowManager.LayoutParams  =  WindowManager.LayoutParams()
        lp.copyFrom(dialog!!.window!!.attributes)
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        val window: Window = dialog!!.window!!
        window.attributes =lp
    }
}