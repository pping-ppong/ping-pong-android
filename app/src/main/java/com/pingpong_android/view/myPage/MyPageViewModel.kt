package com.pingpong_android.view.myPage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pingpong_android.base.BaseViewModel
import com.pingpong_android.model.UserDTO
import com.pingpong_android.model.result.BadgeResultDTO
import com.pingpong_android.model.result.TeamListResultDTO
import com.pingpong_android.model.result.UserResultDTO
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MyPageViewModel : BaseViewModel(){

    // 유저 정보 조회
    private val _userData = MutableLiveData<UserResultDTO>()
    val UserData : LiveData<UserResultDTO>
        get() = _userData
    fun requestUserInfo(token : String) {
        addDisposable(
            instance!!.requestMyPageUserInfo(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _userData.postValue(it)
                },{
                    Log.e("Error", "requestJoin")} )
        )
    }

    // 그룹 조회
    private val _teamListData = MutableLiveData<TeamListResultDTO>()
    val teamListData : LiveData<TeamListResultDTO>
        get() = _teamListData
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

    // 뱃지 조회
    private val _badgeList = MutableLiveData<BadgeResultDTO>()
    val badgeList : LiveData<BadgeResultDTO>
        get() = _badgeList
    fun requestBadgeList(token : String, memberId : Long) {
        addDisposable(
            instance!!.request8Badges(token, memberId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _badgeList.postValue(it)
                },{
                    Log.e("Error", "requestJoin")} )
        )
    }
}