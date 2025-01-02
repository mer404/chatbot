package com.hashmob.aichat.main.intro

import androidx.databinding.DataBindingUtil
import com.hashmob.aichat.R
import com.hashmob.aichat.base.BaseActivity
import com.hashmob.aichat.databinding.ActivityIntroBinding
import com.hashmob.aichat.di.ViewModelFactory
import javax.inject.Inject


class IntroActivity : BaseActivity() {
    lateinit var binding: ActivityIntroBinding

    @Inject
    lateinit var introViewModel: IntroViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    override fun initViewBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_intro)
        introViewModel = viewModelFactory.create(IntroViewModel::class.java)
        binding.lifecycleOwner = this
        introViewModel.initialize(this, binding)
        binding.viewModel = introViewModel
        introViewModel.intro()
        introViewModel.initView()
    }
}