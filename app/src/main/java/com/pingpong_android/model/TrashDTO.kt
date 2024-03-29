package com.pingpong_android.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class TrashDTO (
    @SerializedName("title")
    var title : String = ""
) : Serializable {
    @SerializedName("planId")
    var planId : Long = 0

    @SerializedName("date")
    var date : String = ""

    @SerializedName("status")
    var status : String = ""

    @SerializedName("achievement")
    var achievement : String = ""

    @SerializedName("manager")
    var manager : MemberDTO? = null
}