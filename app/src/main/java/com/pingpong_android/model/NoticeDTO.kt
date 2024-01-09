package com.pingpong_android.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class NoticeDTO (
    @SerializedName("type") var type : String
) : Serializable {

    @SerializedName("message")
    var message : String = ""

    @SerializedName("profileImage")
    var profileImage : String? = null

    @SerializedName("isClicked")
    var isClicked : Boolean = false

    @SerializedName("isAccepted")
    var isAccepted : Boolean = false
}