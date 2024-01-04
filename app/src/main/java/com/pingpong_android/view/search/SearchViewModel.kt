package com.pingpong_android.view.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pingpong_android.base.BaseViewModel
import com.pingpong_android.model.result.FriendListResultDTO
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SearchViewModel : BaseViewModel(){

    private val _userList = MutableLiveData<FriendListResultDTO>()

    val userList : LiveData<FriendListResultDTO>
        get() = _userList

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
}