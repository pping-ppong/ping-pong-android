package com.pingpong_android.view.setting

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pingpong_android.base.BaseViewModel
import com.pingpong_android.model.UserDTO
import com.pingpong_android.model.result.UserResultDTO
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SettingViewModel : BaseViewModel() {

    private val _logoutResult = MutableLiveData<UserResultDTO>()
    val loginResult : LiveData<UserResultDTO>
        get() = _logoutResult

    // 로그인 요청
    fun requestLogout(userDTO: UserDTO) {
        addDisposable(
            instance!!.requestLogout(userDTO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _logoutResult.postValue(it)
                },{

                    Log.e("Error", "requestController")} )
        )
    }
}