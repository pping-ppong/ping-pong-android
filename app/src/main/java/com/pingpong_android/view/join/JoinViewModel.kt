package com.pingpong_android.view.join

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pingpong_android.base.BaseViewModel
import com.pingpong_android.model.result.UserResultDTO
import com.pingpong_android.model.UserDTO
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class JoinViewModel : BaseViewModel(){

    private val _userData = MutableLiveData<UserResultDTO>()
    private val _nickNameCheckResult = MutableLiveData<UserResultDTO>()
    var isReady : MutableLiveData<Boolean> = MutableLiveData(false)
    val userData : LiveData<UserResultDTO>
        get() = _userData
    val nickNameCheckResult : LiveData<UserResultDTO>
        get() = _nickNameCheckResult

    private val _reissueResult = MutableLiveData<UserResultDTO>()
    val reissueResult : LiveData<UserResultDTO>
        get() = _reissueResult

    fun requestNickNameValidation(nickNm : String) {
        addDisposable(
            instance!!.checkValidNickNm(nickNm)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _nickNameCheckResult.postValue(it)
                }, {
                    Log.e("Error", "requestJoin") })
        )
    }
    fun requestJoin(userDTO : UserDTO) {
        addDisposable(
            instance!!.joinApp(userDTO)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _userData.postValue(it)
            },{
                Log.e("Error", "requestJoin")} )
        )
    }

    // 액세스토큰 재발행
    fun requestReissue(userDTO: UserDTO) {
        addDisposable(
            instance!!.requestReissue(userDTO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _reissueResult.postValue(it)
                },{
                    Log.e("Error", "requestController")} )
        )
    }
}