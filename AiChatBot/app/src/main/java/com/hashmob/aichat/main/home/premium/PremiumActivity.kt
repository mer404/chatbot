package com.hashmob.aichat.main.home.premium

import com.hashmob.aichat.base.BaseActivity
import com.hashmob.aichat.databinding.ActivityPremiumBinding
import com.hashmob.aichat.di.ViewModelFactory
import com.hashmob.aichat.main.home.premium.PremiumAdapter.Companion.selectedPosition
import com.hashmob.aichat.util.PremiumUtils
import javax.inject.Inject

class PremiumActivity : BaseActivity() {
    lateinit var binding: ActivityPremiumBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var viewModel: PremiumViewModel
    override fun initViewBinding() {
        binding = ActivityPremiumBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = viewModelFactory.create(PremiumViewModel::class.java)
        binding.lifecycleOwner = this
        viewModel.initialize(this, binding)
        binding.viewModel = viewModel
        viewModel.initView()
        viewModel.setData()
        PremiumUtils.handleBilling(selectedPosition)
    }

}