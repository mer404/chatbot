package com.hashmob.aichat.main.home.tab.topic.tab.composition

import android.content.Intent
import android.view.View
import androidx.databinding.DataBindingUtil
import com.google.android.gms.ads.FullScreenContentCallback
import com.hashmob.aichat.R
import com.hashmob.aichat.base.BaseActivity
import com.hashmob.aichat.constants.Constants
import com.hashmob.aichat.data.main.Resource
import com.hashmob.aichat.data.model.apimodel.WriteResponse
import com.hashmob.aichat.databinding.ActivityCompositionBinding
import com.hashmob.aichat.di.ViewModelFactory
import com.hashmob.aichat.main.home.tab.topic.tab.composition.result.CompositionResultActivity
import com.hashmob.aichat.util.Utils
import javax.inject.Inject


class CompositionActivity : BaseActivity() {
    lateinit var binding: ActivityCompositionBinding
    var topicId = 0
    var subtitle: String = ""

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var viewModel: CompositionViewModel
    override fun initViewBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_composition)
        viewModel = viewModelFactory.create(CompositionViewModel::class.java)
        binding.lifecycleOwner = this
        viewModel.initialize(this, binding)
        binding.viewModel = viewModel
        viewModel.initView()
        viewModel.getCompositionLiveData()
            .observe(this) { response: Resource<WriteResponse> ->
                consumeAPIResponse(response)
            }
        setData()
    }

    private fun setData() {
        val intent = intent
        val topicName = intent.getStringExtra(Constants.TOPIC_NAME)
        topicId = intent.getIntExtra(Constants.SETTING_FLAG, 0)
        subtitle = intent.getStringExtra(Constants.SUBTITLE)!!
        binding.tvTopic.text = topicName.toString()
        if (topicId == 3 || topicId == 5 || topicId == 6) {
            binding.rcvSetting.visibility = View.GONE
            binding.tvSetting.visibility = View.GONE
        }
    }

    private fun consumeAPIResponse(response: Resource<WriteResponse>) {
        when (response.status) {
            Resource.Status.LOADING -> {
                showProgress()
            }

            Resource.Status.ERROR -> {
                hideProgress()
                if (response.error?.message != null) Utils.makeToast(this, response.error.message)
            }

            Resource.Status.SUCCESS -> {
                hideProgress()
                interstitialAds()
                if (response.data?.choices != null) {
                    val intent = Intent(this, CompositionResultActivity::class.java)
                    intent.putExtra(
                        Constants.topic,
                        response.data.choices.first()?.message?.content
                    )
//                    preferences.putInt(Constants.IMAGE_COUNTER, preferences.getInt(Constants.IMAGE_COUNTER) - 1)
                    Utils.setFirebaseData(this,this.preferences.getInt(Constants.allService),false,false)
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
                    binding.edtTopic.text?.clear()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.topicSettingAdapter(topicId)
        binding.edtTopic.clearFocus()
        if (preferences.isProVersion()) {
            binding.tvFreeMessage.visibility = View.GONE
            binding.tvGetPremium.visibility = View.GONE
        }
        binding.tvFreeMessage.text =
            getString(R.string.you_have) + " " + (5 - preferences.getInt(Constants.allService)) + " " + getString(
                R.string.free_messages_left
            )
    }
}