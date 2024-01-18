package com.pingpong_android.view.makeGroup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pingpong_android.base.BaseViewModel
import com.pingpong_android.model.result.TeamResultDTO
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MakeGroupViewModel : BaseViewModel() {

    var isReady : MutableLiveData<Boolean> = MutableLiveData(false)

    private val _teamData = MutableLiveData<TeamResultDTO>()
    val teamData : LiveData<TeamResultDTO>
        get() = _teamData

    // 팀 생성
    fun requestMakeGroup(token : String, name : String, idList: List<Long>) {
        val body = HashMap<String, Any>()
        body.put("name", name)
        body.put("memberId", idList)

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
}