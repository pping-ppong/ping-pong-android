package com.pingpong_android.base

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.kakao.sdk.common.KakaoSdk
import com.pingpong_android.R
import com.pingpong_android.network.RetrofitClient
import io.reactivex.disposables.CompositeDisposable

abstract class BaseActivity <B: ViewDataBinding>(@LayoutRes val layoutId: Int) : AppCompatActivity(){
    lateinit var binding : B
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,layoutId)
        binding.lifecycleOwner = this

        KakaoSdk.init(this, appKey = getString(R.string.kakao_login_app_key))
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}