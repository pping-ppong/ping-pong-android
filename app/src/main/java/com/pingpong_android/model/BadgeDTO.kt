package com.pingpong_android.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BadgeDTO(
    @SerializedName("badgeId") var badgeId : Long
    ) : Serializable {

    @SerializedName("badgeName")
    var badgeName: String = ""
}