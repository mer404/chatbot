package com.hashmob.aichat.main.home.ui.drawer

import android.content.Intent
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.ViewModel
import com.hashmob.aichat.R
import com.hashmob.aichat.constants.Constants
import com.hashmob.aichat.data.repository.MainRepository
import com.hashmob.aichat.databinding.ActivityPrivacyPolicyBinding
import javax.inject.Inject

class PrivacyPolicyViewModel @Inject constructor(var authRepository: MainRepository) : ViewModel() {
    lateinit var activity: PrivacyPolicyActivity
    lateinit var binding: ActivityPrivacyPolicyBinding
    var intent: Intent? = null
    fun initialize(
        activity: PrivacyPolicyActivity,
        binding: ActivityPrivacyPolicyBinding,
        intent: Intent
    ) {
        this.activity = activity
        this.binding = binding
        this.intent = intent
        binding.webView.settings.javaScriptEnabled = true
        initView()
    }

    fun initView() {
        val navigate = intent?.getIntExtra(Constants.web_view, 0)
        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url!!)
                return true
            }
        }
        if (navigate == 0) {
            binding.header.tvTitle.text = activity.getString(R.string.privacy_policy)
            binding.webView.loadUrl(activity.preferences.getRemoteConfig()?.androidPrivacyPolicy.toString())
        } else {
            binding.header.tvTitle.text = activity.getString(R.string.terms_of_use)
            binding.webView.loadUrl(activity.preferences.getRemoteConfig()?.androidTemsService.toString())
        }
        binding.header.ivDrawer.visibility = View.GONE
        binding.header.tv.visibility = View.GONE
        binding.header.tvTitle.visibility = View.VISIBLE
        binding.header.ivBack.setOnClickListener {
            activity.onBackPressedDispatcher.onBackPressed()
        }
    }

}