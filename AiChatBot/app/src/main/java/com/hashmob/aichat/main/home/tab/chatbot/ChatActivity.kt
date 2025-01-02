package com.hashmob.aichat.main.home.tab.chatbot

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.hashmob.aichat.R
import com.hashmob.aichat.base.BaseActivity
import com.hashmob.aichat.constants.Constants
import com.hashmob.aichat.constants.Constants.REQUEST_CODE_SPEECH_INPUT
import com.hashmob.aichat.data.main.Resource
import com.hashmob.aichat.data.model.apimodel.ChoicesItem
import com.hashmob.aichat.data.model.apimodel.WriteResponse
import com.hashmob.aichat.databinding.ActivityChatBinding
import com.hashmob.aichat.di.ViewModelFactory
import com.hashmob.aichat.util.LogUtils
import com.hashmob.aichat.util.Utils
import kotlinx.android.synthetic.main.activity_chat.edtMessage
import java.util.Locale
import java.util.Objects
import javax.inject.Inject

class ChatActivity : BaseActivity() {
    lateinit var binding: ActivityChatBinding
    lateinit var textToSpeech: TextToSpeech

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var viewModel: ChatViewModel
    override fun initViewBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat)
        viewModel = viewModelFactory.create(ChatViewModel::class.java)
        binding.lifecycleOwner = this
        viewModel.initialize(this, binding)
        binding.viewModel = viewModel
        viewModel.getChatLiveData().observe(this) { response: Resource<WriteResponse> ->
            consumeAPIResponse(response)
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
//                preferences.putInt(Constants.IMAGE_COUNTER, preferences.getInt(Constants.IMAGE_COUNTER) - 1)
                if (response.data?.choices != null) {
                    viewModel.chatList.add(
                        ChoicesItem(
                            this.getString(R.string.stop),
                            0,
                            response.data.choices.first()?.message?.content,
                            false,
                            null
                        )
                    )
                    binding.rcvChat.adapter?.notifyItemInserted(viewModel.chatList.size - 1)
                    binding.rcvChat.scrollToPosition(viewModel.chatList.size - 1)
                    binding.edtMessage.text?.clear()
                    binding.edtMessage.clearFocus()
                }
                Utils.setFirebaseData(this,this.preferences.getInt(Constants.allService),false,false)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        textToSpeech.shutdown()
    }

    override fun onResume() {
        super.onResume()
        textToSpeech = TextToSpeech(this) {}
        textToSpeech.language = Locale.US
        binding.edtMessage.clearFocus()
        binding.header.ivLanguage.visibility = View.VISIBLE
        Glide.with(this)
            .load(preferences.getData().language?.get(preferences.getInt(Constants.LANGUAGE_POSITION))?.img_link)
            .centerCrop().placeholder(R.drawable.ic_uncheck_premium)
            .diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.header.ivLanguage)
        if (preferences.isProVersion()) {
            binding.tvFreeMessage.visibility = View.GONE
            binding.viewPremiumText.visibility = View.GONE
            binding.tvGetPremium.visibility = View.GONE
        }
        binding.tvFreeMessage.text =
            getString(R.string.you_have) + " " + (5 - preferences.getInt(Constants.allService)) + " " + getString(
                R.string.free_messages_left
            )
    }


    fun speechToText() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speak_to_text))
        try {
            ActivityCompat.startActivityForResult(this, intent, REQUEST_CODE_SPEECH_INPUT, Bundle())
        } catch (e: Exception) {
            LogUtils.Print("TAGS", e.message.toString())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                val res: ArrayList<String> =
                    data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<String>
                edtMessage.setText(Objects.requireNonNull(res)[0])
            }
        }
    }
}