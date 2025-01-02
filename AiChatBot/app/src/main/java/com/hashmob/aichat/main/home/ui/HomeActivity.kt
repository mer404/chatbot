package com.hashmob.aichat.main.home.ui

import android.view.View
import androidx.databinding.DataBindingUtil
import com.hashmob.aichat.R
import com.hashmob.aichat.base.BaseActivity
import com.hashmob.aichat.databinding.ActivityHomeBinding
import com.hashmob.aichat.di.ViewModelFactory
import javax.inject.Inject


class HomeActivity : BaseActivity() {
    lateinit var binding: ActivityHomeBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var viewModel: HomeViewModel

    override fun initViewBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        viewModel = viewModelFactory.create(HomeViewModel::class.java)
        binding.lifecycleOwner = this
        viewModel.initialize(this, binding)
        binding.viewModel = viewModel
        viewModel.initView()
        viewModel.setAdapter()
    }

    override fun onResume() {
        super.onResume()
        if (preferences.isProVersion()) {
            binding.premiumContainer.visibility = View.GONE
            binding.drawerLayout.tvRestorePurchases.visibility = View.GONE
            adView?.destroy()
            binding.adViewContainer.visibility = View.GONE
        }
    }
}