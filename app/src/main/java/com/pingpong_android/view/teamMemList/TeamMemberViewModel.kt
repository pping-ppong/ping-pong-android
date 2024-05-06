package com.pingpong_android.view.teamMemList

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pingpong_android.base.BaseViewModel
import com.pingpong_android.model.result.FriendListResultDTO
import com.pingpong_android.model.result.NoResultDTO
import com.pingpong_android.model.result.ResultDTO
import com.pingpong_android.model.result.TeamResultDTO
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TeamMemberViewModel : BaseViewModel() {

    // 팀 멤버 전체 조회
    private val _teamMemberResult = MutableLiveData<FriendListResultDTO>()
    val teamMemberResult : LiveData<FriendListResultDTO>
        get() = _teamMemberResult
    fun requestTeamMemberList(token : String, teamId : Long) {
        addDisposable(
            instance!!.requestTeamAllMember(token, teamId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _teamMemberResult.postValue(it)
                },{
                    Log.e("Error", "requestController")
                } )
        )
    }

    // 팀원 방출
    private val _emitResult = MutableLiveData<TeamResultDTO>()
    val emitResult : LiveData<TeamResultDTO>
        get() = _emitResult
    fun requestEmitMember(token : String, teamId : Long, emitterId : Long) {
        addDisposable(
            instance!!.emitMember(token, teamId, emitterId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _emitResult.postValue(it)
                },{
                    Log.e("Error", "requestController")
                } )
        )
    }

    fun requestEmitAlarm(token : String,  teamId: Long, memberId : Long) {
        val body = HashMap<String, Long>()
        body["teamId"] = teamId
        body["memberId"] = memberId

        addDisposable(
            instance!!.requestEmitAlarm(token, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                },{
                    Log.e("Error", "requestController")} )
        )
    }

    // 팀원 -> 방장 위임
    private val _changeHostResult = MutableLiveData<TeamResultDTO>()
    val changeHostResult : LiveData<TeamResultDTO>
        get() = _changeHostResult
    fun requestChangeHost(token : String, delegatorId : Long, teamId : Long) {
        addDisposable(
            instance!!.changeTeamHost(token, teamId, delegatorId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _changeHostResult.postValue(it)
                },{
                    Log.e("Error", "requestController")
                } )
        )
    }

    fun requestGetHostAlarm(token : String, memberId : Long, teamId: Long) {
        val body = HashMap<String, Long>()
        body["teamId"] = teamId
        body["memberId"] = memberId

        addDisposable(
            instance!!.requestGotHostAlarm(token, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                },{
                    Log.e("Error", "requestController")} )
        )
    }

    // 친구 신청
    private val _applyResult = MutableLiveData<NoResultDTO>()
    val applyResult : LiveData<NoResultDTO>
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
}