package com.pingpong_android.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.kakao.sdk.common.KakaoSdk
import com.pingpong_android.BuildConfig
import com.pingpong_android.R
import com.pingpong_android.utils.PreferenceUtil
import io.reactivex.disposables.CompositeDisposable

abstract class BaseActivity <B: ViewDataBinding>(@LayoutRes val layoutId: Int) : AppCompatActivity(){
    lateinit var binding : B
    private val compositeDisposable = CompositeDisposable()

    companion object {
        lateinit var prefs: PreferenceUtil
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,layoutId)
        binding.lifecycleOwner = this

        KakaoSdk.init(this, appKey = BuildConfig.KAKAO_LOGIN_APP_KEY)
        prefs = PreferenceUtil(applicationContext)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}