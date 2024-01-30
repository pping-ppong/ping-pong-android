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

    var isSelected : Boolean = false
}