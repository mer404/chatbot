package com.hashmob.aichat.main.home.tab.topic.tab.composition.result

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import com.hashmob.aichat.base.BaseActivity
import com.hashmob.aichat.constants.Constants
import com.hashmob.aichat.databinding.ActivityCompositionResultBinding
import com.hashmob.aichat.di.ViewModelFactory
import com.hashmob.aichat.util.Utils
import javax.inject.Inject

class CompositionResultActivity : BaseActivity() {
    lateinit var binding: ActivityCompositionResultBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var viewModel: ResultViewModel
    override fun initViewBinding() {
        binding = ActivityCompositionResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = viewModelFactory.create(ResultViewModel::class.java)
        binding.lifecycleOwner = this
        viewModel.initialize(this, binding)
        binding.viewModel = viewModel
        viewModel.initView()
        val compositionTopic = intent.getStringExtra(Constants.topic)
        binding.tvResult.text = compositionTopic?.trim()

        binding.header.ivPdf.setOnClickListener {
            if (Utils.checkPermission(this, WRITE_EXTERNAL_STORAGE) && Utils.checkPermission(
                    this,
                    READ_EXTERNAL_STORAGE
                )
            ) {
                Utils.generatePdf(this, binding.tvResult.text.toString())
            } else {
                Utils.showPermissionsDialog(
                    this, arrayOf(
                        WRITE_EXTERNAL_STORAGE,
                        READ_EXTERNAL_STORAGE
                    ), Constants.REQUEST_CODE_CAMERA_PERMISSION
                )
            }
        }
    }

    public override fun onDestroy() {
        if (viewModel.tts != null) {
            viewModel.tts!!.stop()
            viewModel.tts!!.shutdown()
        }
        super.onDestroy()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == Constants.REQUEST_CODE_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty()) {
                Utils.generatePdf(this, binding.tvResult.text.toString())
            } else {
                Utils.shouldShowRequestPermissionRationale(this, WRITE_EXTERNAL_STORAGE)
            }
        }
    }
}