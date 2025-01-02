package com.hashmob.aichat.main.home.ui.drawer

import androidx.databinding.DataBindingUtil
import com.hashmob.aichat.R
import com.hashmob.aichat.base.BaseActivity
import com.hashmob.aichat.databinding.ActivityPrivacyPolicyBinding
import com.hashmob.aichat.di.ViewModelFactory
import javax.inject.Inject

class PrivacyPolicyActivity : BaseActivity() {
    lateinit var binding: ActivityPrivacyPolicyBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var viewModel: PrivacyPolicyViewModel

    override fun initViewBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_privacy_policy)
        viewModel = viewModelFactory.create(PrivacyPolicyViewModel::class.java)
        binding.lifecycleOwner = this
        viewModel.initialize(this, binding,intent)
        binding.viewModel = viewModel

    }
}