package com.pingpong_android.view.webView

import android.os.Bundle
import com.pingpong_android.R
import com.pingpong_android.base.BaseActivity
import com.pingpong_android.base.Constants.Companion.INTENT_EXTRA_WEB_URL
import com.pingpong_android.databinding.ActivityWebviewBinding

class WebViewActivity : BaseActivity<ActivityWebviewBinding>(R.layout.activity_webview, TransitionMode.NONE) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = WebViewViewModel()
        binding.activity = this

        val url : String = intent.getStringExtra(INTENT_EXTRA_WEB_URL)!!
        binding.webview.loadUrl(url)
        binding.topPanel.setLeftClickListener(listener = {onBackPressed()})
    }
}