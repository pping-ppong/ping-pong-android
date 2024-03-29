package com.pingpong_android.view.makeTeam

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pingpong_android.base.BaseViewModel
import com.pingpong_android.model.result.TeamResultDTO
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MakeTeamViewModel : BaseViewModel() {

    var isReady : MutableLiveData<Boolean> = MutableLiveData(false)

    private val _teamData = MutableLiveData<TeamResultDTO>()
    val teamData : LiveData<TeamResultDTO>
        get() = _teamData

    // 팀 생성
    fun requestMakeGroup(token : String, name : String, idList: List<Long>) {
        val body = HashMap<String, Any>()
        body["name"] = name
        body["memberId"] = idList

        addDisposable(
            instance!!.requestMakeGroup(token, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _teamData.postValue(it)
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