package com.hashmob.aichat.main.home.tab.topic.ui

import com.hashmob.aichat.base.BaseActivity
import com.hashmob.aichat.databinding.ActivityCategoriesChatBinding
import com.hashmob.aichat.di.ViewModelFactory
import javax.inject.Inject

class CategoriesChatActivity : BaseActivity() {
    lateinit var binding: ActivityCategoriesChatBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var viewModel: CategoriesChatViewModel

    override fun initViewBinding() {
        binding = ActivityCategoriesChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = viewModelFactory.create(CategoriesChatViewModel::class.java)
        binding.lifecycleOwner = this
        viewModel.initialize(this, binding)
        binding.viewModel = viewModel
        viewModel.initView()
        viewModel.category()
    }
}