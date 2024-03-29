package com.pingpong_android.view.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pingpong_android.base.BaseViewModel
import com.pingpong_android.model.UserDTO
import com.pingpong_android.model.result.AchieveResultDTO
import com.pingpong_android.model.result.ResultDTO
import com.pingpong_android.model.result.TeamListResultDTO
import com.pingpong_android.model.result.UserResultDTO
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainViewModel : BaseViewModel(){

    // 안읽은 알림 확인
    private val _noticeState = MutableLiveData<ResultDTO>()
    val noticeState : LiveData<ResultDTO>
        get() = _noticeState
    fun requestUnReadNotice(token : String) {
        addDisposable(
            instance!!.requestUnReadNotice(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _noticeState.postValue(it)
                },{
                    Log.e("Error", "requestController")} )
        )
    }

    // 유저 정보 불러오기
    private val _userData = MutableLiveData<UserResultDTO>()
    val userData : LiveData<UserResultDTO>
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

    // 캘린더 성취율 조회
    private val _achieveResult = MutableLiveData<AchieveResultDTO>()
    val achieveResult : LiveData<AchieveResultDTO>
        get() = _achieveResult
    fun requestMonthAchievement(token : String, startDate : String, endDate : String) {
        addDisposable(
            instance!!.requestMainCalendarAll(token, startDate, endDate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _achieveResult.postValue(it)
                },{
                    Log.e("Error", "requestJoin")} )
        )
    }

    // 할 일 조회
    private val _plansResult = MutableLiveData<TeamListResultDTO>()
    val plansResult : LiveData<TeamListResultDTO>
        get() = _plansResult
    fun requestPlans(token : String, date : String) {
        addDisposable(
            instance!!.requestMainPlans(token, date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _plansResult.postValue(it)
                },{
                    Log.e("Error", "requestJoin")} )
        )
    }

    // 할 일 완료 / 미완료 / 삭제 요청
    private val _planRequestResult = MutableLiveData<ResultDTO>()
    val planRequestResult : LiveData<ResultDTO>
        get() = _planRequestResult
    fun requestPlanComplete(token : String, teamId : Long, planId : Long) {
        addDisposable(
            instance!!.requestPlanComplete(token, teamId, planId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _planRequestResult.postValue(it)
                },{
                    Log.e("Error", "requestJoin")} )
        )
    }
    fun requestPlanIncomplete(token : String, teamId : Long, planId : Long) {
        addDisposable(
            instance!!.requestPlanIncomplete(token, teamId, planId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _planRequestResult.postValue(it)
                },{
                    Log.e("Error", "requestJoin")} )
        )
    }

    fun requestPlanDelete(token : String, teamId : Long, planId : Long) {
        addDisposable(
            instance!!.deletePlanToTrash(token, teamId, planId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _planRequestResult.postValue(it)
                },{
                    Log.e("Error", "requestJoin")} )
        )
    }
}