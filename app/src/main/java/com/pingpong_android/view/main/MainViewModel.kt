package com.pingpong_android.view.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pingpong_android.base.BaseViewModel
import com.pingpong_android.model.UserDTO
import com.pingpong_android.model.result.ResultDTO
import com.pingpong_android.model.result.TeamListResultDTO
import com.pingpong_android.model.result.UserResultDTO
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainViewModel : BaseViewModel(){

    private val _noticeState = MutableLiveData<ResultDTO>()
    val noticeState : LiveData<ResultDTO>
        get() = _noticeState

    private val _userData = MutableLiveData<UserResultDTO>()
    val userData : LiveData<UserResultDTO>
        get() = _userData

    fun requestUnReadNotice(token : String) {
        addDisposable(
            instance!!.requestUnReadNotice(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _noticeState.postValue(it)
                },{
                    Log.e("Error", "requestController")} )
        )
    }

    fun requestUserInfo(token : String, user : UserDTO) {
        addDisposable(
            instance!!.requestMyPageUserInfo(token, user.memberId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _userData.postValue(it)
                },{
                    Log.e("Error", "requestJoin")} )
        )
    }
}