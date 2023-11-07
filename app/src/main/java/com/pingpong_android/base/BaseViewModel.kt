package com.pingpong_android.base

import androidx.lifecycle.ViewModel
import com.pingpong_android.network.RetrofitClient
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel: ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    val instance = RetrofitClient.getClient(false)

    fun addDisposable(disposable: Disposable){
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

}