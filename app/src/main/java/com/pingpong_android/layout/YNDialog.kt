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
import com.pingpong_android.databinding.LayoutYesNoDialogBinding

class YNDialog(private var content : String, private var menus : List<String>) : DialogFragment() {

    private lateinit var binding : LayoutYesNoDialogBinding
    private lateinit var buttonClickListener: OnButtonClickListener // 클릭 이벤트 실행

    companion object {
        const val TAG = "YesNoDialog"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LayoutYesNoDialogBinding.inflate(inflater, container, false)
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

        binding.content.text = content
        binding.firstBtn.text = menus[0]
        binding.secondBtn.text = menus[1]
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