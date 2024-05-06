package com.pingpong_android.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class NoticeDTO (
    @SerializedName("type") var type : String
) : Serializable {

    @SerializedName("notificationId")
    var notificationId : String = ""

    @SerializedName("message")
    var message : String = ""

    @SerializedName("profileImage")
    var profileImage : String? = null

    @SerializedName("memberId")
    var memberId : Long = 0

    @SerializedName("teamId")
    var teamId : Long = 0

    @SerializedName("isClicked")
    var isClicked : Boolean = false

    @SerializedName("isAccepted")
    var isAccepted : Boolean = false

    @SerializedName("createdAt")
    var createdAt : String = ""

    @SerializedName("result")
    var notificationExists : Boolean = false
}