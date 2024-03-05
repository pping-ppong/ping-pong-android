package com.pingpong_android.view.teamCalendar

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pingpong_android.base.BaseViewModel
import com.pingpong_android.model.AchieveDTO
import com.pingpong_android.model.TodoDTO
import com.pingpong_android.model.result.AchieveResultDTO
import com.pingpong_android.model.result.ResultDTO
import com.pingpong_android.model.result.TeamResultDTO
import com.pingpong_android.model.result.TodoResultDTO
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.time.LocalDate

class TeamCalendarViewModel : BaseViewModel() {

    // 할 일 추가
    private val _addTodoResult = MutableLiveData<TodoResultDTO>()
    val addTodoResult : LiveData<TodoResultDTO>
        get() = _addTodoResult
    fun requestAddTodo(token : String, teamId : Long, todoDTO: TodoDTO) {
        addDisposable(
            instance!!.requestAddTodo(token, teamId, todoDTO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _addTodoResult.postValue(it)
                },{
                    Log.e("Error", "requestJoin")} )
        )
    }

    // 캘린더 성취율 조회
    private val _achieveResult = MutableLiveData<AchieveResultDTO>()
    val achieveResult : LiveData<AchieveResultDTO>
        get() = _achieveResult
    fun requestMonthAchievement(token : String, teamId : Long, startDate : LocalDate, endDate : LocalDate) {
        val body = HashMap<String, String>()
        body["startDate"] = startDate.toString()
        body["endDate"] = endDate.toString()

        addDisposable(
            instance!!.requestTeamCalendarAll(token, teamId, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _achieveResult.postValue(it)
                },{
                    Log.e("Error", "requestJoin")} )
        )
    }

    // 할 일 조회
    private val _plansResult = MutableLiveData<TeamResultDTO>()
    val plansResult : LiveData<TeamResultDTO>
        get() = _plansResult
    fun requestPlans(token : String, teamId: Long, date : String) {
        addDisposable(
            instance!!.requestTeamPlans(token, teamId, date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _plansResult.postValue(it)
                },{
                    Log.e("Error", "requestJoin")} )
        )
    }

    // 할 일 완료 / 미완료 요청
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
            instance!!.deletePlan(token, teamId, planId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _planRequestResult.postValue(it)
                },{
                    Log.e("Error", "requestJoin")} )
        )
    }
}