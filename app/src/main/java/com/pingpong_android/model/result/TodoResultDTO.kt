package com.pingpong_android.model.result

import com.google.gson.annotations.SerializedName
import com.pingpong_android.model.TodoDTO

data class TodoResultDTO(
    @SerializedName("isSuccess")
    var isSuccess : Boolean,

    @SerializedName("code")
    var code : Int,

    @SerializedName("message")
    var message : String,

    @SerializedName("result")
    var todoDTO : TodoDTO
)