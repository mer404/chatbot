package com.hashmob.aichat.main.home.tab.chatbot

import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.provider.Settings.Secure
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener
import android.speech.tts.UtteranceProgressListener
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.hashmob.aichat.R
import com.hashmob.aichat.constants.Constants
import com.hashmob.aichat.data.main.Resource
import com.hashmob.aichat.data.model.apimodel.ChoicesItem
import com.hashmob.aichat.data.model.apimodel.WriteResponse
import com.hashmob.aichat.data.repository.MainRepository
import com.hashmob.aichat.databinding.ActivityChatBinding
import com.hashmob.aichat.main.home.tab.topic.tab.composition.chooselanguage.ChooseLanguageActivity
import com.hashmob.aichat.main.home.tab.topic.tab.utils.model.MessageRequestModel
import com.hashmob.aichat.util.FormValidation
import com.hashmob.aichat.util.Utils
import com.hashmob.aichat.util.Utils.Companion.premiumScreenIntent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class ChatViewModel @Inject constructor(var authRepository: MainRepository) : ViewModel(),
    OnUtteranceCompletedListener {
    lateinit var activity: ChatActivity
    lateinit var binding: ActivityChatBinding
    lateinit var chatList: ArrayList<ChoicesItem>
    val arraylist = ArrayList<String>()
    private var rewardedAd: RewardedAd? = null
    private val messagesParams = java.util.ArrayList<MessageRequestModel>()
    lateinit var adapter: ChatAdapter
    lateinit var holder: ChatAdapter.ViewHolder

    //LIVE DATA
    private val liveData = MutableLiveData<Resource<WriteResponse>>()
    var prompt: MutableLiveData<String> = MutableLiveData()

    //API
    private val compositeDisposable = CompositeDisposable()
    fun initialize(chatActivity: ChatActivity, binding: ActivityChatBinding) {
        this.activity = chatActivity
        this.binding = binding
        activity.textToSpeech = TextToSpeech(activity){}
        initView()
        setData()
        activity.loadBanner(activity)
        loadRewardedAd()
    }

    private fun loadRewardedAd() {
        if (activity.preferences.isProVersion()) return
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(activity,
            activity.preferences.getRemoteConfig()?.androidRewardedAds.toString(),
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    rewardedAd = null
                }

                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                }
            })
    }

    fun getChatLiveData(): MutableLiveData<Resource<WriteResponse>> {
        return liveData
    }

    private fun callChatApi() {
        messagesParams.clear()
        messagesParams.add(
            MessageRequestModel(
                activity.getString(R.string.you_will_be_provided_with_text_and_your_task_is_to_chat_with_user_do_not_use_any_regular_text_do_your_best_with_your_answer_in) + activity.preferences.getString(
                    Constants.LANGUAGE
                ), activity.getString(R.string.role_system)
            )
        )
        messagesParams.add(
            MessageRequestModel(
                prompt.value.toString(), activity.resources.getString(R.string.user)
            )
        )
        if (isValidInput()) {
            val params = HashMap<String, Any>()
            params[Constants.model] = "gpt-3.5-turbo"
            params[Constants.messages] = messagesParams
            params[Constants.temperature] = 0.5
            compositeDisposable.add(authRepository.aiChat(params).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).doOnSubscribe {
                    liveData.setValue(Resource.loading())
                }.subscribe({ response: WriteResponse? ->
                    liveData.setValue(Resource.success(response))
                }, { throwable: Throwable? ->
                    liveData.setValue(Resource.error(throwable!!))
                })
            )
        }
    }


    private fun isValidInput(): Boolean {
        val validation = FormValidation(activity)
        return if (!validation.isEmpty(
                binding.edtMessage.text?.toString()?.trim(),
                activity.resources.getString(R.string.please_enter_message)
            )
        ) {
            binding.edtMessage.requestFocus()
            false
        } else {
            true
        }

    }

    fun initView() {
        activity.preferences.getString(Constants.LANGUAGE).toString()
        binding.header.ivDrawer.visibility = View.GONE
        binding.header.tv.visibility = View.GONE
        binding.header.tvTitle.visibility = View.VISIBLE
        binding.header.ivPdf.visibility = View.VISIBLE
        binding.header.ivPdf.setImageResource(R.drawable.ic_translate_message)
        binding.header.tvTitle.text = activity.getString(R.string.chat)
        binding.header.ivBack.setOnClickListener {
            activity.onBackPressedDispatcher.onBackPressed()
        }
        arrayListOf(binding.header.ivPdf, binding.header.ivLanguage).forEach {
            it.setOnClickListener {
                val intent = Intent(activity, ChooseLanguageActivity::class.java)
                activity.startActivity(intent)
            }
        }
    }

    fun setData() {
        chatList = ArrayList()
        val model = (ChoicesItem(
            activity.getString(R.string.stop),
            0,
            activity.getString(R.string.hi_there),
            false,
            null,
            false
        ))
        chatList.add(model)
        adapter = ChatAdapter(
            activity,
            chatList,
            this::onCopyClick,
            this::onForwardClick,
            this::onSpeechClick,
            this::onPauseClick
        )
        binding.rcvChat.layoutManager = LinearLayoutManager(activity)
        binding.rcvChat.setHasFixedSize(true)
        binding.rcvChat.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun onCopyClick(position: Int) {
        val clipManager: ClipboardManager =
            activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipManager.text = chatList[position].text
        Utils.makeToast(
            activity, activity.getString(R.string.copied_to_clipboard)
        )
    }

    private fun onSpeechClick(position: Int, holder: ChatAdapter.ViewHolder) {
        for (i in 0 until chatList.size) {
            chatList[i].isPlaying = false
            adapter.notifyDataSetChanged()
        }
        chatList[position].isPlaying = true
        adapter.notifyDataSetChanged()
        activity.textToSpeech.speak(
            chatList[position].text.toString(),
            TextToSpeech.QUEUE_FLUSH,
            null,
            TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID
        )
        activity.textToSpeech.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                holder.ivPlay.visibility = View.INVISIBLE
                holder.ivPause.visibility = View.VISIBLE
            }

            override fun onDone(utteranceId: String?) {
                holder.ivPause.visibility = View.INVISIBLE
                holder.ivPlay.visibility = View.VISIBLE
            }

            override fun onError(utteranceId: String?) {
            }

            override fun onStop(utteranceId: String?, interrupted: Boolean) {
                holder.ivPause.visibility = View.INVISIBLE
                holder.ivPlay.visibility = View.VISIBLE
            }
        })
    }

    private fun onPauseClick(holder: ChatAdapter.ViewHolder) {
        activity.textToSpeech.stop()
        holder.ivPause.visibility = View.INVISIBLE
        holder.ivPlay.visibility = View.VISIBLE
    }

    private fun onForwardClick(position: Int) {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_TEXT, chatList[position].text)
        shareIntent.type = "text/plain"
        activity.startActivity(
            Intent.createChooser(
                shareIntent, activity.getString(R.string.send_to)
            )
        )
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.tvGetPremium -> {
                premiumScreenIntent(activity)
            }

            R.id.btnSend -> {
                if (isValidInput()) {
                    chatList.add(
                        ChoicesItem(
                            activity.getString(R.string.stop), 0, prompt.value, true, null
                        )
                    )
                    binding.rcvChat.adapter?.notifyItemInserted(chatList.size - 1)
                    binding.rcvChat.scrollToPosition(chatList.size - 1)

//                    if (activity.preferences.isProVersion() || activity.preferences.getInt(Constants.IMAGE_COUNTER) > 0) {
//                        callChatApi()
//                    } else {
//                        premiumScreenIntent(activity)
//                    }
                    if (activity.preferences.isProVersion()) {
                        callChatApi()
                    } else if (activity.preferences.getInt(Constants.allService) < 5){
                        callChatApi()
                    }else{
                        premiumScreenIntent(activity)
                    }
//                    if (activity.preferences.isProVersion() || activity.preferences.getInt(Constants.IMAGE_COUNTER) == 0) return
                    if (activity.preferences.isProVersion() || activity.preferences.getInt(Constants.allService) > 5) return
                    rewardedAd?.let { ad ->
                        ad.show(activity) {
                            binding.edtMessage.text?.clear()
                            loadRewardedAd()
                        }
                    } ?: run {}
                }
            }

            R.id.ivVoiceMessage -> activity.speechToText()
        }
    }
    override fun onUtteranceCompleted(utteranceId: String?) {
    }
}
