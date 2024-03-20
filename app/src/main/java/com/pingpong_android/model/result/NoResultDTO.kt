package com.pingpong_android.model.result

import com.google.gson.annotations.SerializedName

data class NoResultDTO (
    @SerializedName("isSuccess")
    var isSuccess : Boolean,

    @SerializedName("code")
    var code : Int,

    @SerializedName("type")
    var type : String,

    @SerializedName("message")
    var message : String
)