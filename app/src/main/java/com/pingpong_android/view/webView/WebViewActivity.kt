package com.pingpong_android.view.webView

import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebViewClient
import com.pingpong_android.R
import com.pingpong_android.base.BaseActivity
import com.pingpong_android.base.Constants.Companion.INTENT_EXTRA_WEB_URL
import com.pingpong_android.databinding.ActivityWebviewBinding

class WebViewActivity : BaseActivity<ActivityWebviewBinding>(R.layout.activity_webview, TransitionMode.NONE) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = WebViewViewModel()
        binding.activity = this

        initView()
    }

    private fun initView() {
        // webView
        val url : String = intent.getStringExtra(INTENT_EXTRA_WEB_URL)!!
        binding.webview.apply {
            settings.javaScriptEnabled = true
            settings.javaScriptCanOpenWindowsAutomatically = false
            settings.defaultTextEncodingName = "UTF-8"
            webViewClient = WebViewClient()
            webChromeClient = WebChromeClient()
            settings.domStorageEnabled = true
            settings.builtInZoomControls = false
            settings.displayZoomControls = false
            settings.cacheMode = WebSettings.LOAD_NO_CACHE
            settings.setRenderPriority(WebSettings.RenderPriority.HIGH)
        }
        binding.webview.loadUrl(url)
        // button
        binding.topPanel.setLeftClickListener(listener = {onBackPressed()})
    }
}