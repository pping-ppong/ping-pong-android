package com.pingpong_android.model.result

import com.google.gson.annotations.SerializedName

data class ImageResultDTO (
    @SerializedName("isSuccess")
    var isSuccess : Boolean,

    @SerializedName("code")
    var code : Int,

    @SerializedName("message")
    var message : String,

    @SerializedName("result")
    var imgList : List<String>
)