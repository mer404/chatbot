package com.hashmob.aichat.main.home.tab.topic.tab.composition.chooselanguage

import androidx.databinding.DataBindingUtil
import com.hashmob.aichat.R
import com.hashmob.aichat.base.BaseActivity
import com.hashmob.aichat.databinding.ActivityChooseLanguageBinding
import com.hashmob.aichat.di.ViewModelFactory
import javax.inject.Inject

class ChooseLanguageActivity : BaseActivity() {
    lateinit var binding: ActivityChooseLanguageBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var viewModel: LanguageViewModel

    override fun initViewBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_choose_language)
        viewModel = viewModelFactory.create(LanguageViewModel::class.java)
        binding.lifecycleOwner = this
        viewModel.initialize(this, binding)
        binding.viewModel = viewModel
        viewModel.initView()
        viewModel.setData()
    }
}