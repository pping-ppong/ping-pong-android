package com.pingpong_android.view.setting.information

import android.content.Intent
import android.os.Bundle
import com.pingpong_android.BuildConfig
import com.pingpong_android.R
import com.pingpong_android.base.BaseActivity
import com.pingpong_android.base.Constants
import com.pingpong_android.databinding.ActivityInformationBinding
import com.pingpong_android.view.webView.WebViewActivity

class InformationActivity : BaseActivity<ActivityInformationBinding>(R.layout.activity_information, TransitionMode.RIGHT) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = InformationViewModel()
        binding.activity = this

        initView()
    }

    private fun initView() {
        binding.topPanel.setLeftClickListener(listener = {onBackPressed()})
        binding.appVersion.text = BuildConfig.VERSION_NAME
    }

    fun goToWebView(type : Int) {
        var url = if (type == 1)
            getString(R.string.url_term_personal_information)
        else
            getString(R.string.url_term_service)

        val intent = Intent(this, WebViewActivity::class.java)
        intent.putExtra(Constants.INTENT_EXTRA_WEB_URL, url)
        startActivity(intent)
    }
}