package com.pingpong_android.view.setting.account

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pingpong_android.base.BaseViewModel
import com.pingpong_android.model.result.ResultDTO
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AccountViewModel : BaseViewModel() {
    private val _result = MutableLiveData<ResultDTO>()
    val result : LiveData<ResultDTO>
        get() = _result

    // 로그인 요청
    fun requestDeleteAccount(token : String) {
        addDisposable(
            instance!!.requestDeleteAccount(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _result.postValue(it)
                },{
                    Log.e("Error", "requestController")} )
        )
    }
}