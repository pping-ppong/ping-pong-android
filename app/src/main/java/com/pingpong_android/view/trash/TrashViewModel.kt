package com.pingpong_android.view.trash

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pingpong_android.base.BaseViewModel
import com.pingpong_android.model.result.ResultDTO
import com.pingpong_android.model.result.TrashResultDTO
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TrashViewModel : BaseViewModel() {

    // 휴지통 전체 조회
    private val _trashResult = MutableLiveData<TrashResultDTO>()
    val trashResult : LiveData<TrashResultDTO>
        get() = _trashResult
    fun requestTrashAll(token : String, teamId: Long) {
        addDisposable(
            instance!!.requestTrashAll(token, teamId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _trashResult.postValue(it)
                },{
                    Log.e("Error", "requestJoin")} )
        )
    }

    // 할 일 복구하기
    private val _result = MutableLiveData<ResultDTO>()
    val result : LiveData<ResultDTO>
        get() = _result
    fun requestRestorePlan(token : String, teamId: Long, planId : Long) {
        addDisposable(
            instance!!.restorePlan(token, teamId, planId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _result.postValue(it)
                },{
                    Log.e("Error", "requestJoin")} )
        )
    }

    // 할 일 하나 삭제하기
    fun requestDeleteOnePlan(token : String, teamId: Long, planId : Long) {
        addDisposable(
            instance!!.deleteOnePlan(token, teamId, planId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _result.postValue(it)
                },{
                    Log.e("Error", "requestJoin")} )
        )
    }

    // 할 일 전체 삭제하기
    fun requestDeleteAllPlan(token : String, teamId: Long) {
        addDisposable(
            instance!!.deleteAllPlan(token, teamId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _result.postValue(it)
                },{
                    Log.e("Error", "requestJoin")} )
        )
    }
}