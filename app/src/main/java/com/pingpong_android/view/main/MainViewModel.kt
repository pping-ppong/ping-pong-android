package com.pingpong_android.view.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pingpong_android.base.BaseViewModel
import com.pingpong_android.model.result.ResultDTO
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainViewModel : BaseViewModel(){

    private val _noticeState = MutableLiveData<ResultDTO>()
    val noticeState : LiveData<ResultDTO>
        get() = _noticeState

    fun requestNotReadNotice(token : String) {
        addDisposable(
            instance!!.requestNotReadNotice(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _noticeState.postValue(it)
                },{
                    Log.e("Error", "requestController")} )
        )
    }
}