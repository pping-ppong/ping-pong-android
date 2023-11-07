package com.pingpong_android.view.join

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pingpong_android.base.BaseViewModel
import com.pingpong_android.model.ResultDTO
import com.pingpong_android.model.UserDTO
import com.pingpong_android.network.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class JoinViewModel : BaseViewModel(){

    private val instance = RetrofitClient.getClient(false)

    private val _userData = MutableLiveData<ResultDTO>()
    private val _nickNameCheckResult = MutableLiveData<ResultDTO>()
    var isReady : MutableLiveData<Boolean> = MutableLiveData(false)
    val userData : LiveData<ResultDTO>
        get() = _userData
    val nickNameCheckResult : LiveData<ResultDTO>
        get() = _nickNameCheckResult

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
}