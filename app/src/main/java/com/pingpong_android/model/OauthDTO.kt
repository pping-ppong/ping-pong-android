package com.pingpong_android.model

import com.google.gson.annotations.SerializedName
import com.kakao.sdk.auth.model.OAuthToken
import java.io.Serializable

data class OauthDTO(
    @SerializedName("socialType") var socialType : String,
    var kakaoToken : OAuthToken
    ) : Serializable {
    init {
        kakaoToken.also {
            this.code = it.idToken.toString()
            this.accessToken = it.accessToken
            this.refreshToken = it.refreshToken
        }
    }

    @SerializedName("code") var code : String

    @SerializedName("accessToken") var accessToken : String

    @SerializedName("refreshToken") var refreshToken : String
}