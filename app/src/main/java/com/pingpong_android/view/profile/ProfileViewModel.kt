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

    private val _userResult = MutableLiveData<UserResultDTO>()
    val userResult : LiveData<UserResultDTO>
        get() = _userResult

    private val _result = MutableLiveData<ResultDTO>()
    val result : LiveData<ResultDTO>
        get() = _result


    // 프로필 조회
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
    fun requestFriendShip(token : String, applicantId : Long, respondentId : Long) {
        val body = HashMap<String, Long>()
        body.put("applicantId", applicantId)
        body.put("respondentId", respondentId)

        addDisposable(
            instance!!.requestFriendShip(token, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _result.postValue(it)
                },{
                    Log.e("Error", "requestController")} )
        )
    }
}