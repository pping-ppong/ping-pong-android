package com.pingpong_android.view.myPageActivity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pingpong_android.base.BaseViewModel
import com.pingpong_android.model.UserDTO
import com.pingpong_android.model.result.TeamListResultDTO
import com.pingpong_android.model.result.UserResultDTO
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MyPageViewModel : BaseViewModel(){

    private val _userData = MutableLiveData<UserResultDTO>()
    private val _teamListData = MutableLiveData<TeamListResultDTO>()
    val UserData : LiveData<UserResultDTO>
        get() = _userData
    val TeamList : LiveData<TeamListResultDTO>
        get() = _teamListData
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

    fun requestUserTeamList(token : String) {
        addDisposable(
            instance!!.requestUserTeams(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _teamListData.postValue(it)
                },{
                    Log.e("Error", "requestJoin")} )
        )
    }
}