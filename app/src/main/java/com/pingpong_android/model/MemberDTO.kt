package com.pingpong_android.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MemberDTO(
    @SerializedName("memberId") var memberId : Long
) : Serializable {

    @SerializedName("nickname")
    var nickName : String = ""

    @SerializedName("profileImage")
    var profileImage : String = ""

    @SerializedName("hostId")
    var hostId : Long = -1

    @SerializedName("friendStatus")
    var friendStatus : String = ""

    @SerializedName("status")
    var status : String = ""
}