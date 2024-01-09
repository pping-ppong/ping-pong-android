package com.pingpong_android.view.intro

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pingpong_android.base.BaseViewModel
import com.pingpong_android.model.OauthDTO
import com.pingpong_android.model.UserDTO
import com.pingpong_android.model.result.UserResultDTO
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class IntroViewModel : BaseViewModel() {

    private val _userOauth = MutableLiveData<UserDTO>()
    val userOauth : LiveData<UserDTO>
        get() = _userOauth

    private val _loginResult = MutableLiveData<UserResultDTO>()
    val loginResult : LiveData<UserResultDTO>
        get() = _loginResult

    private val _reissueResult = MutableLiveData<UserResultDTO>()
    val reissueResult : LiveData<UserResultDTO>
        get() = _reissueResult


    // 가입된 유저인지 확인
    fun requestSocialInfo(oauthDTO: OauthDTO) {
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

    // 로그인 요청
    fun requestLogin(userDTO: UserDTO) {
        addDisposable(
            instance!!.requestLogin(userDTO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _loginResult.postValue(it)
                },{
                    Log.e("Error", "requestController")} )
        )
    }
}