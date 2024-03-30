package com.pingpong_android.layout

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.pingpong_android.databinding.LayoutHostDialogBinding
import com.pingpong_android.model.MemberDTO

class HostDialog(private var menus : List<String>, private val member : MemberDTO) : DialogFragment() {

    private lateinit var binding : LayoutHostDialogBinding
    private lateinit var buttonClickListener: OnButtonClickListener // 클릭 이벤트 실행

    companion object {
        const val TAG = "HostDialog"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LayoutHostDialogBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        return binding.root
    }

    interface OnButtonClickListener{
        fun onFirstClicked()
        fun onSecondClicked()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.firstBtn.setOnClickListener { buttonClickListener.onFirstClicked() }
        binding.secondBtn.setOnClickListener { buttonClickListener.onSecondClicked() }

        binding.memberNm.text = member.nickName
        binding.firstBtn.text = menus[0]
        binding.secondBtn.text = menus[1]
    }

    fun setButtonClickListener(buttonClickListener: OnButtonClickListener) {
        this.buttonClickListener = buttonClickListener
    }
}