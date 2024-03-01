package com.pingpong_android.view.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pingpong_android.base.BaseViewModel
import com.pingpong_android.model.result.ResultDTO
import com.pingpong_android.model.result.UserResultDTO
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ProfileViewModel : BaseViewModel() {

    // 프로필 조회
    private val _userResult = MutableLiveData<UserResultDTO>()
    val userResult : LiveData<UserResultDTO>
        get() = _userResult
    fun requestOthersProfile(token : String, memberId : Long) {
        addDisposable(
            instance!!.requestOthersProfile(token, memberId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _userResult.postValue(it)
                },{
                    Log.e("Error", "requestController")} )
        )
    }

    // 친구 신청
    private val _applyResult = MutableLiveData<ResultDTO>()
    val applyResult : LiveData<ResultDTO>
        get() = _applyResult
    fun requestFriendShip(token : String, applicantId : Long, respondentId : Long) {
        val body = HashMap<String, Long>()
        body.put("applicantId", applicantId)
        body.put("respondentId", respondentId)

        addDisposable(
            instance!!.requestFriendShip(token, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _applyResult.postValue(it)
                },{
                    Log.e("Error", "requestController")} )
        )
    }

    // 친구 신청 알림
    fun requestAlarmFriend(token : String, memberId : Long) {
        addDisposable(
            instance!!.requestAlarmFriend(token, memberId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                },{
                    Log.e("Error", "requestController")} )
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