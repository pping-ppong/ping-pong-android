package com.pingpong_android.view.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pingpong_android.base.BaseViewModel
import com.pingpong_android.model.result.FriendListResultDTO
import com.pingpong_android.model.result.LogResultDTO
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SearchViewModel : BaseViewModel(){

    private val _userList = MutableLiveData<FriendListResultDTO>()

    val userList : LiveData<FriendListResultDTO>
        get() = _userList

    private val _logList = MutableLiveData<LogResultDTO>()

    val logList : LiveData<LogResultDTO>
        get() = _logList

    // 검색 요청
    fun searchUserWithNickNm(token : String, nickNam : String) {
        addDisposable(
            instance!!.searchUserWithNickNm(token, nickNam)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _userList.postValue(it)
                },{
                    Log.e("Error", "requestJoin")} )
        )
    }

    // 검색 키워드 저장
    fun addSearchLog(token : String, id : Long) {
        addDisposable(
            instance!!.addSearchLog(token, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                },{
                    Log.e("Error", "requestJoin")} )
        )
    }

    // 검색 키워드 불러오기
    fun requestSearchLog(token : String) {
        addDisposable(
            instance!!.requestSearchLog(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _logList.postValue(it)
                },{
                    Log.e("Error", "requestJoin")} )
        )
    }
}