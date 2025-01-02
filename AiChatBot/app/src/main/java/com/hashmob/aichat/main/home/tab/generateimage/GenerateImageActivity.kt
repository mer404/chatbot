package com.hashmob.aichat.main.home.tab.generateimage

import android.content.Intent
import android.view.View
import com.google.android.gms.ads.FullScreenContentCallback
import com.hashmob.aichat.R
import com.hashmob.aichat.base.BaseActivity
import com.hashmob.aichat.constants.Constants
import com.hashmob.aichat.data.main.Resource
import com.hashmob.aichat.data.model.apimodel.GenerateImageResponse
import com.hashmob.aichat.databinding.ActivityGenerateImageBinding
import com.hashmob.aichat.di.ViewModelFactory
import com.hashmob.aichat.main.home.tab.generateimage.generateimageresult.GenerateImageResultActivity
import com.hashmob.aichat.util.Utils
import javax.inject.Inject

class GenerateImageActivity : BaseActivity() {
    lateinit var binding: ActivityGenerateImageBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var viewModel: GenerateImageViewModel

    override fun initViewBinding() {
        binding = ActivityGenerateImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = viewModelFactory.create(GenerateImageViewModel::class.java)
        binding.lifecycleOwner = this
        viewModel.initialize(this, binding)
        binding.viewModel = viewModel
        viewModel.initView()
        viewModel.prompt()
        viewModel.artStyle()
        viewModel.searchView()
        viewModel.getGenerateImageLiveData()
            .observe(this) { response: Resource<GenerateImageResponse> ->
                consumeAPIResponse(response)
            }
    }

    private fun consumeAPIResponse(response: Resource<GenerateImageResponse>) {
        when (response.status) {
            Resource.Status.LOADING -> {
                showProgress()
                interstitialAds()
            }

            Resource.Status.ERROR -> {
                hideProgress()
                if (response.error?.message != null) Utils.makeToast(this, response.error.message)
            }

            Resource.Status.SUCCESS -> {
                hideProgress()
                if (response.data?.data != null) {
                    if (interstitialAd != null) {
                        interstitialAd?.show(this)
                    }
//                    preferences.putInt(Constants.IMAGE_COUNTER, preferences.getInt(Constants.IMAGE_COUNTER) - 1)
                    val intent = Intent(this, GenerateImageResultActivity::class.java)
                    intent.putExtra(Constants.url, response.data.data.first()?.url.toString())
                    if (!preferences.isProVersion()) {
                        interstitialAd?.fullScreenContentCallback =
                            object : FullScreenContentCallback() {
                                override fun onAdDismissedFullScreenContent() {
                                    startActivity(intent)
                                }
                            }
                    } else {
                        startActivity(intent)
                    }
                    if (preferences.isProVersion()) {
                        Utils.setFirebaseData(this,this.preferences.getInt(Constants.allService),false,true)
                    }else{
                        Utils.setFirebaseData(this,this.preferences.getInt(Constants.allService),false,false)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.edtPrompt.clearFocus()
        binding.edtPrompt.text?.clear()
        if (preferences.isProVersion()) {
            binding.tvFreeMessage.text =
                getString(R.string.you_have) + " " + (10 - preferences.getInt(Constants.generateImageService)) + " " + getString(
                    R.string.free_messages_left
                )
            binding.tvGetPremium.visibility = View.GONE
        } else {
            binding.tvFreeMessage.text =
                getString(R.string.you_have) + " " + (5 - preferences.getInt(Constants.allService)) + " " + getString(
                    R.string.free_messages_left
                )
            binding.tvGetPremium.visibility = View.VISIBLE
        }
    }
}