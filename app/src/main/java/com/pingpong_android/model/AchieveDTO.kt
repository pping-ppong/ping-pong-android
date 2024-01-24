package com.pingpong_android.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AchieveDTO(
    @SerializedName("date")
    var date : String = ""
    ) : Serializable {

    @SerializedName("achievement")
    var achievement : Double = 0.0

}
