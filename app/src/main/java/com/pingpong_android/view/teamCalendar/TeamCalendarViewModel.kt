package com.pingpong_android.view.teamCalendar

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pingpong_android.base.BaseViewModel
import com.pingpong_android.model.TodoDTO
import com.pingpong_android.model.result.TodoResultDTO
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TeamCalendarViewModel : BaseViewModel() {

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


}