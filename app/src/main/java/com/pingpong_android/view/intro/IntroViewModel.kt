package com.pingpong_android.view.intro

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pingpong_android.base.BaseViewModel
import com.pingpong_android.model.OauthDTO
import com.pingpong_android.model.UserDTO
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class IntroViewModel : BaseViewModel() {

    private val _userOauth = MutableLiveData<UserDTO>()
    val userOauth : LiveData<UserDTO>
        get() = _userOauth

    fun requestSocialInfo(oauthDTO: UserDTO) {
        addDisposable(
            instance!!.getSocialInfo(oauthDTO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _userOauth.postValue(it.userDTO)
                },{
                    Log.e("Error", "requestController")} )
        )
    }
}