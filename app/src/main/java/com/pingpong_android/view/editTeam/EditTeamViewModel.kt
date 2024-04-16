package com.pingpong_android.view.editTeam

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pingpong_android.base.BaseViewModel
import com.pingpong_android.model.result.ResultDTO
import com.pingpong_android.model.result.TeamResultDTO
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class EditTeamViewModel : BaseViewModel() {
    var isReady : MutableLiveData<Boolean> = MutableLiveData(false)

    // 팀 수정
    private val _editTeamData = MutableLiveData<TeamResultDTO>()
    val editTeamData : LiveData<TeamResultDTO>
        get() = _editTeamData
    fun requestEditTeam(token : String, teamId : Long, name : String, idList: List<Long>) {
        val body = HashMap<String, Any>()
        body["name"] = name
        body["memberId"] = idList

        addDisposable(
            instance!!.editTeam(token, teamId, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _editTeamData.postValue(it)
                },{
                    Log.e("Error", "requestJoin")} )
        )
    }

    // 팀 삭제
    private val _deleteTeamData = MutableLiveData<ResultDTO>()
    val deleteTeamData : LiveData<ResultDTO>
        get() = _deleteTeamData
    fun requestDeleteTeam(token : String, teamId : Long) {
        addDisposable(
            instance!!.deleteTeamForever(token, teamId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _deleteTeamData.postValue(it)
                },{
                    Log.e("Error", "requestJoin")} )
        )
    }

    // 팀 초대된 사람들 알림 보내기
    fun requestTeamInviteAlarm(token : String, teamId : Long, memberId : Long) {
        val body = HashMap<String, Long>()
        body["teamId"] = teamId
        body["memberId"] = memberId

        addDisposable(
            instance!!.requestTeamInviteAlarm(token, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                },{
                    Log.e("Error", "requestJoin")} )
        )
    }
}