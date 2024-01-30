package com.pingpong_android.layout

import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.pingpong_android.databinding.LayoutDateMemberBinding
import com.pingpong_android.model.MemberDTO
import java.time.LocalDate
import java.time.ZoneId

class DateMemberSetDialog(val memberList : List<MemberDTO>, val date : LocalDate) : DialogFragment() {

    private lateinit var binding : LayoutDateMemberBinding
    private lateinit var buttonClickListener: OnButtonClickListener // 클릭 이벤트 실행
    private lateinit var selectedMember : MemberDTO
    private lateinit var selectedDate : LocalDate

    companion object {
        const val TAG = "DateMemberSetDialog"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LayoutDateMemberBinding.inflate(inflater, container, false)
        return binding.root
    }

    interface OnButtonClickListener{
        fun onCancelClicked()
        fun onConfirmClicked()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initMember()
    }

    private fun initView() {
        binding.calender.date = date.toEpochDay()
    }

    private fun initMember() {

    }

    // 클릭 이벤트 설정
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