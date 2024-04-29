package com.pingpong_android.view.intro

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pingpong_android.base.BaseViewModel
import com.pingpong_android.model.UserDTO
import com.pingpong_android.model.result.UserResultDTO
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class IntroViewModel : BaseViewModel() {

    private var introActivity : IntroActivity? = null
    fun setActivity(introActivity : IntroActivity) {
        this.introActivity = introActivity
    }

    private val _loginResult = MutableLiveData<UserResultDTO>()
    val loginResult : LiveData<UserResultDTO>
        get() = _loginResult

    // 로그인 요청
    fun requestLogin(userDTO: UserDTO) {
        addDisposable(
            instance!!.requestLogin(userDTO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _loginResult.postValue(it)
                },{
                    introActivity?.apiError()
                    Log.e("Error", "requestController")} )
        )
    }
}