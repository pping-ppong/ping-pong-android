package com.pingpong_android.model.result

import com.google.gson.annotations.SerializedName
import com.pingpong_android.model.TeamDTO

data class TeamResultDTO (
    @SerializedName("isSuccess")
    var isSuccess : Boolean,

    @SerializedName("code")
    var code : Int,

    @SerializedName("message")
    var message : String,

    @SerializedName("result")
    var team : TeamDTO
)