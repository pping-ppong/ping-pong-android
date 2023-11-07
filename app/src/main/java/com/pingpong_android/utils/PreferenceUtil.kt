package com.pingpong_android.utils

import android.content.Context
import android.content.SharedPreferences
import com.pingpong_android.model.UserDTO

class PreferenceUtil(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("prefs_name", Context.MODE_PRIVATE)

    fun getString(key: String, defValue: String): String {
        return prefs.getString(key, defValue).toString()
    }

    fun setString(key: String, str: String) {
        prefs.edit().putString(key, str).apply()
    }

    fun saveUser(user: UserDTO){
        if (user.socialId.isNotEmpty())
            prefs.edit().putString("socialId", user.socialId).apply()
        if (user.email.isNotEmpty())
            prefs.edit().putString("email", user.email).apply()
        if (user.nickName.isNotEmpty())
            prefs.edit().putString("nickName", user.nickName).apply()
        if (user.profileImage.isNotEmpty())
            prefs.edit().putString("profileImage", user.profileImage).apply()
        if (user.memberId.isNotEmpty())
            prefs.edit().putString("memberId", user.memberId).apply()
        if (user.socialType.isNotEmpty())
            prefs.edit().putString("socialType", user.socialType).apply()
    }

    fun getUser(): UserDTO{
        return UserDTO(prefs.getString("socialId", null).toString()).apply {
            email = prefs.getString("email", null).toString()
            nickName = prefs.getString("nickName", null).toString()
            profileImage = prefs.getString("profileImage", null).toString()
            memberId = prefs.getString("memberId", null).toString()
            socialType = prefs.getString("socialType", null).toString()

        }
    }
}