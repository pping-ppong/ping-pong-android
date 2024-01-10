package com.pingpong_android.view.notice

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pingpong_android.base.BaseViewModel
import com.pingpong_android.model.result.NoticeResultDTO
import com.pingpong_android.model.result.ResultDTO
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class NoticeViewModel : BaseViewModel(){

    private val _noticeList = MutableLiveData<NoticeResultDTO>()
    val noticeList : LiveData<NoticeResultDTO>
        get() = _noticeList

    private val _result = MutableLiveData<ResultDTO>()
    val result : LiveData<ResultDTO>
        get() = _result

    fun requestAllNotice(token : String) {
        addDisposable(
            instance!!.requestAllNotice(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _noticeList.postValue(it)
                },{
                    Log.e("Error", "requestController")} )
        )
    }

    fun acceptFriendShip(token : String, respondentId : Long) {
        addDisposable(
            instance!!.acceptFriendShip(token, respondentId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _result.postValue(it)
                },{
                    Log.e("Error", "requestController")} )
        )
    }

    fun refuseFriendShip(token : String, respondentId : Long) {
        addDisposable(
            instance!!.refuseFriendShip(token, respondentId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _result.postValue(it)
                },{
                    Log.e("Error", "requestController")} )
        )
    }

}