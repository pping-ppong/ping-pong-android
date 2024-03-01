package com.pingpong_android.view.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pingpong_android.base.BaseViewModel
import com.pingpong_android.model.OauthDTO
import com.pingpong_android.model.result.UserResultDTO
import com.pingpong_android.model.UserDTO
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LoginViewModel : BaseViewModel() {

    private val _userData = MutableLiveData<UserResultDTO>()
    val userData : LiveData<UserResultDTO>
        get() = _userData

    // 로그인 요청
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