package com.pingpong_android.layout

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.pingpong_android.databinding.LayoutDateMemberBinding
import com.pingpong_android.model.MemberDTO
import com.pingpong_android.view.adapter.MemberHorizontalAdapter
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField
import kotlin.math.max

class DateMemberSetDialog(val memberList : List<MemberDTO>, val date : LocalDate) : DialogFragment() {

    private lateinit var binding : LayoutDateMemberBinding
    private lateinit var buttonClickListener: OnButtonClickListener // 클릭 이벤트 실행
    lateinit var selectedDate : LocalDate
    val memberAdapter = MemberHorizontalAdapter(memberList, true)

    companion object {
        const val TAG = "DateMemberSetDialog"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LayoutDateMemberBinding.inflate(inflater, container, false)
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

        initView()
        initMember()
    }

    private fun initView() {
        // 버튼
        binding.btnNo.setOnClickListener { buttonClickListener.onCancelClicked() }
        binding.btnYes.setOnClickListener { buttonClickListener.onConfirmClicked() }

        // 캘린더
        date.withDayOfMonth(1).toString()
        selectedDate = date // 기본
        // 해당 달만 조회
        binding.calender.date = LocalDate.parse(date.toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            .atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        binding.calender.minDate = LocalDate.parse(date.withDayOfMonth(1).toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            .atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        binding.calender.maxDate = LocalDate.parse(date.withDayOfMonth(date.lengthOfMonth()).toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            .atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        binding.calender.setOnDateChangeListener { view, year, month, dayOfMonth ->
            // 날짜 선택 시 갱신
            selectedDate = LocalDate.of(year, month+1, dayOfMonth)
        }
    }

    private fun initMember() {
        val memberLayoutManager = FlexboxLayoutManager(getContext())
        memberLayoutManager.flexDirection = FlexDirection.ROW
        memberLayoutManager.justifyContent = JustifyContent.FLEX_START

        binding.memberRv.apply {
            layoutManager = memberLayoutManager
            adapter = memberAdapter
        }
    }

    fun getSelectMember() : MemberDTO {
        return memberAdapter.getSelectedMember()
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