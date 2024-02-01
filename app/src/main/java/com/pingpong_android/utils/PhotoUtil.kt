package com.pingpong_android.utils

import java.text.SimpleDateFormat
import java.util.*

class PhotoUtil {

    fun makeImageFileName(): String? {
        val simpleDateFormat = SimpleDateFormat("yyyyMMdd_hhmmss")
        val date = Date()
        val strDate = simpleDateFormat.format(date)
        return "$strDate.png"
    }
}