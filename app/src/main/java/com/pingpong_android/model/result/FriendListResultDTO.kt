package com.pingpong_android.model.result

import com.google.gson.annotations.SerializedName
import com.pingpong_android.model.MemberDTO
import com.pingpong_android.model.UserDTO

data class FriendListResultDTO (
    @SerializedName("isSuccess")
    var isSuccess : Boolean,

    @SerializedName("code")
    var code : Int,

    @SerializedName("message")
    var message : String,

    @SerializedName("result")
    var friendList : List<MemberDTO>
)