package com.pingpong_android.model

import android.provider.ContactsContract.CommonDataKinds.Nickname
import androidx.annotation.Nullable
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserDTO(
    @SerializedName("socialId") var socialId: String
) : Serializable {

    constructor(socialId: String, _email : String, _nickname: String, _profileImage : String, _memberId : String) : this (socialId) {
        email = _email
        nickName = _nickname
        profileImage = _profileImage
        memberId = _memberId
    }

    @SerializedName("email")
    var email: String = ""

    @SerializedName("nickname")
    var nickName: String = ""

    @SerializedName("profileImage")
    var profileImage: String = ""

    @SerializedName("memberId")
    var memberId: String = ""

    @SerializedName("SocialType")
    var socialType : String = ""

    @SerializedName("SocialToken")
    var socialToken : String = ""
}