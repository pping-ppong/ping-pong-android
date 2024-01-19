package com.pingpong_android.layout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pingpong_android.databinding.LayoutBottomSheetDialogBinding

class ModalBottomSheetDialog(private var menu : List<String>) : BottomSheetDialogFragment() {

    private lateinit var binding : LayoutBottomSheetDialogBinding
    private lateinit var dialogInterface: ModalBottomSheetDialogInterface

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = LayoutBottomSheetDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }

    interface ModalBottomSheetDialogInterface {
        fun onFirstClickListener()
        fun onSecondClickListener()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.firstBtn.text = menu[0]
        binding.secondBtn.text = menu[1]

        binding.firstBtn.setOnClickListener {
            dialogInterface.onFirstClickListener()
        }

        binding.secondBtn.setOnClickListener {
            dialogInterface.onSecondClickListener()
        }
    }

    fun setDialogInterface(dialogInterface: ModalBottomSheetDialogInterface) {
        this.dialogInterface = dialogInterface
    }
}