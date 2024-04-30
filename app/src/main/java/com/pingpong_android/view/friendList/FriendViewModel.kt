package com.pingpong_android.view.friendList

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pingpong_android.base.BaseViewModel
import com.pingpong_android.model.result.FriendListResultDTO
import com.pingpong_android.model.result.NoResultDTO
import com.pingpong_android.model.result.ResultDTO
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class FriendViewModel : BaseViewModel(){

    // 친구 전체 조회
    private val _friendListData = MutableLiveData<FriendListResultDTO>()
    val friendList : LiveData<FriendListResultDTO>
        get() = _friendListData
    fun requestUserFriendList(token : String) {
        addDisposable(
            instance!!.requestUserFriendList(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _friendListData.postValue(it)
                },{
                    Log.e("Error", "requestJoin")} )
        )
    }

    // 친구 끊기
    private val _deleteResult = MutableLiveData<ResultDTO>()
    val deleteResult : LiveData<ResultDTO>
        get() = _deleteResult
    fun deleteFriendShip(token : String, memberId : Long) {
        addDisposable(
            instance!!.deleteFriendShip(token, memberId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _deleteResult.postValue(it)
                },{
                    Log.e("Error", "requestController")} )
        )
    }
}