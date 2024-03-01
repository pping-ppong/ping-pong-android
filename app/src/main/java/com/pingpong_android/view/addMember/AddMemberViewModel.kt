package com.pingpong_android.view.addMember

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pingpong_android.base.BaseViewModel
import com.pingpong_android.model.result.FriendListResultDTO
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AddMemberViewModel : BaseViewModel() {

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

}