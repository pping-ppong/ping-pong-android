package com.pingpong_android.view.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pingpong_android.base.BaseViewModel
import com.pingpong_android.model.OauthDTO
import com.pingpong_android.model.ResultDTO
import com.pingpong_android.model.UserDTO
import com.pingpong_android.network.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LoginViewModel : BaseViewModel() {

    private val _userOauth = MutableLiveData<UserDTO>()
    val userOauth : LiveData<UserDTO>
        get() = _userOauth

    private val _userData = MutableLiveData<ResultDTO>()
    val userData : LiveData<ResultDTO>
        get() = _userData

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

    fun requestLogin(userDTO: UserDTO) {
        addDisposable(
            instance!!.requestLogin(userDTO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _userData.postValue(it)
                },{
                    Log.e("Error", "requestController")} )
        )
    }
}