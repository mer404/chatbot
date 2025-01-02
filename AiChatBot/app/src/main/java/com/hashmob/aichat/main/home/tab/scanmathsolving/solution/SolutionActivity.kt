package com.hashmob.aichat.main.home.tab.scanmathsolving.solution

import androidx.databinding.DataBindingUtil
import com.hashmob.aichat.R
import com.hashmob.aichat.base.BaseActivity
import com.hashmob.aichat.constants.Constants
import com.hashmob.aichat.databinding.ActivitySolutionBinding
import com.hashmob.aichat.di.ViewModelFactory
import javax.inject.Inject

class SolutionActivity : BaseActivity() {
    lateinit var binding: ActivitySolutionBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var viewModel: SolutionViewModel
    override fun initViewBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_solution)
        viewModel = viewModelFactory.create(SolutionViewModel::class.java)
        binding.lifecycleOwner = this
        viewModel.initialize(this, binding)
        binding.viewModel = viewModel
        val answerTopic = intent.getStringExtra(Constants.answer)
        binding.txtAnswer.text = answerTopic?.trim()
    }
}