package com.pingpong_android.view.teamList

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pingpong_android.base.BaseViewModel
import com.pingpong_android.model.result.TeamListResultDTO
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TeamListViewModel : BaseViewModel(){

    private val _teamListData = MutableLiveData<TeamListResultDTO>()
    val teamList : LiveData<TeamListResultDTO>
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
}