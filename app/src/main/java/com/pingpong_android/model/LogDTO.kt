package com.pingpong_android.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LogDTO(
    @SerializedName("key") var key : String
) : Serializable {

    @SerializedName("nickname")
    var nickName: String = ""

    @SerializedName("profileImage")
    var profileImage: String = ""

    @SerializedName("memberId")
    var memberId: String = ""
}