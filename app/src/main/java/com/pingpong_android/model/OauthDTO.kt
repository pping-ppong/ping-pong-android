package com.pingpong_android.model

import com.google.gson.annotations.SerializedName
import com.kakao.sdk.auth.model.OAuthToken
import java.io.Serializable

data class OauthDTO(
    @SerializedName("socialType") var socialType : String,
    var code : String
    ) : Serializable {

    constructor(socialType: String, code : String, _accessToken : String, _refreshToken: String) : this (socialType, code) {
        accessToken = _accessToken
        refreshToken = _refreshToken
    }

    @SerializedName("accessToken")
    var accessToken : String = ""

    @SerializedName("refreshToken")
    var refreshToken : String = ""
}