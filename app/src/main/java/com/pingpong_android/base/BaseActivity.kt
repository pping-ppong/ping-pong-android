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

abstract class BaseActivity <B: ViewDataBinding>(@LayoutRes val layoutId: Int, private val transitionMode: TransitionMode = TransitionMode.NONE) : AppCompatActivity(){
    lateinit var binding : B
    private val compositeDisposable = CompositeDisposable()

    enum class TransitionMode {
        NONE,
        RIGHT,
        LEFT,
        VERTICAL
    }

    companion object {
        lateinit var prefs: PreferenceUtil
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 화면 전환
        when (transitionMode) {
            TransitionMode.RIGHT -> overridePendingTransition(R.anim.right_enter, R.anim.none)
            TransitionMode.LEFT -> overridePendingTransition(R.anim.left_enter, R.anim.none)
            TransitionMode.VERTICAL -> overridePendingTransition(R.anim.vertical_enter, R.anim.none)
            else -> Unit
        }

        binding = DataBindingUtil.setContentView(this,layoutId)
        binding.lifecycleOwner = this

        KakaoSdk.init(this, appKey = BuildConfig.KAKAO_LOGIN_APP_KEY)
        prefs = PreferenceUtil(applicationContext)
    }

    override fun finish() {
        super.finish()

        when (transitionMode) {
            TransitionMode.RIGHT -> overridePendingTransition(R.anim.none, R.anim.right_exit)
            TransitionMode.LEFT -> overridePendingTransition(R.anim.none, R.anim.left_exit)
            TransitionMode.VERTICAL -> overridePendingTransition(R.anim.none, R.anim.vertical_exit)
            else -> Unit
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (isFinishing) {
            when (transitionMode) {
                TransitionMode.RIGHT -> overridePendingTransition(R.anim.none, R.anim.right_exit)
                TransitionMode.LEFT -> overridePendingTransition(R.anim.none, R.anim.left_exit)
                TransitionMode.VERTICAL -> overridePendingTransition(R.anim.none, R.anim.vertical_exit)
                else -> Unit
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}