package com.hashmob.aichat.main.home.tab.topic.tab.composition

import android.annotation.SuppressLint
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hashmob.aichat.R
import com.hashmob.aichat.constants.Constants
import com.hashmob.aichat.data.main.Resource
import com.hashmob.aichat.data.model.apimodel.WriteResponse
import com.hashmob.aichat.data.repository.MainRepository
import com.hashmob.aichat.databinding.ActivityCompositionBinding
import com.hashmob.aichat.main.home.tab.topic.tab.composition.chooselanguage.ChooseLanguageActivity
import com.hashmob.aichat.main.home.tab.topic.tab.utils.adapter.CommonSettingAdapter
import com.hashmob.aichat.main.home.tab.topic.tab.utils.adapter.LengthAdapter
import com.hashmob.aichat.main.home.tab.topic.tab.utils.model.LengthModel
import com.hashmob.aichat.main.home.tab.topic.tab.utils.model.MessageRequestModel
import com.hashmob.aichat.main.home.tab.topic.tab.utils.model.TopicSettingModel
import com.hashmob.aichat.util.FormValidation
import com.hashmob.aichat.util.Utils
import com.hashmob.aichat.util.Utils.Companion.premiumScreenIntent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class CompositionViewModel @Inject constructor(var authRepository: MainRepository) : ViewModel() {
    @SuppressLint("StaticFieldLeak")
    lateinit var activity: CompositionActivity
    lateinit var binding: ActivityCompositionBinding
    private var adapter: CommonSettingAdapter? = null
    private lateinit var list: ArrayList<TopicSettingModel>
    private val messagesParams = java.util.ArrayList<MessageRequestModel>()
    private lateinit var lengthList: ArrayList<LengthModel>
    private var adapterLength: LengthAdapter? = null

    //LIVE DATA
    private val liveData = MutableLiveData<Resource<WriteResponse>>()
    var topic: MutableLiveData<String> = MutableLiveData()

    //API
    private val compositeDisposable = CompositeDisposable()
    fun initialize(activity: CompositionActivity, binding: ActivityCompositionBinding) {
        this.activity = activity
        this.binding = binding
        this.activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        activity.loadBanner(activity)
        activity.interstitialAds()
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.btnSubmit -> {
//                if (activity.preferences.isProVersion() || activity.preferences.getInt(Constants.IMAGE_COUNTER) > 0) {
//                    callCompositionApi()
//                } else {
//                    premiumScreenIntent(activity)
//                }
                if (activity.preferences.isProVersion()) {
                    callCompositionApi()
                } else if (activity.preferences.getInt(Constants.allService) < 5){
                    callCompositionApi()
                }else{
                    premiumScreenIntent(activity)
                }
            }

            R.id.tvGetPremium -> {
                premiumScreenIntent(activity)
            }
        }
    }

    fun getCompositionLiveData(): MutableLiveData<Resource<WriteResponse>> {
        return liveData
    }

    private fun callCompositionApi() {
        val selectedLanguage = activity.preferences.getString(Constants.LANGUAGE)
        val selectedLength = activity.preferences.getString(Constants.LENGTH)
        val messagesArr = ArrayList<HashMap<String, String>>()
        messagesParams.clear()
        if (activity.subtitle.isNotEmpty()) {
            val messageObj = HashMap<String, String>()
            messageObj[Constants.role] = Constants.role_system
            messageObj[Constants.content] = activity.subtitle + when (activity.topicId) {
                5 -> ""
                6 -> "$selectedLanguage."
                7 -> activity.getString(R.string.in_language, selectedLanguage)
                else -> {
                    activity.getString(R.string.content_you_will_be_provided) + selectedLanguage + if (activity.topicId != 3 && activity.topicId != 5 && activity.topicId != 6 && activity.topicId != 7) {
                        activity.getString(R.string.do_your_best_in_to_give_answer) + selectedLength + activity.getString(
                            R.string.sentence
                        )
                    } else {
                        ""
                    }
                }
            }
            messagesArr.add(messageObj)
        } else {
            val messageObj = HashMap<String, String>()
            messageObj[Constants.role] = Constants.role_system
            messageObj[Constants.content] =
                activity.getString(R.string.content_you_will_be_provided) + selectedLanguage + activity.getString(
                    R.string.do_your_best_in_to_give_answer
                ) + selectedLength + activity.getString(
                    R.string.sentence
                )
            messagesArr.add(messageObj)
        }
        val messageObj = HashMap<String, String>()
        messageObj[Constants.role] = Constants.role_user
        messageObj[Constants.content] = topic.value.toString()
        messagesArr.add(messageObj)
        if (isValidInput(activity.topicId)) {
            if (activity.interstitialAd != null) {
                activity.interstitialAd?.show(activity)
            }
            val params = HashMap<String, Any>()
            params[Constants.model] = activity.getString(R.string.params_model)
            params[Constants.messages] = messagesArr
            params[Constants.temperature] = 0.5
            params[Constants.max_tokens] =
                if (activity.topicId != 3 && activity.topicId != 5 && activity.topicId != 6 && activity.topicId != 7) {
                    activity.preferences.getInt(Constants.topicLength)
                } else {
                    150
                }
            compositeDisposable.add(authRepository.aiChat(params).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).doOnSubscribe {
                    liveData.setValue(Resource.loading())
                }.subscribe({ response: WriteResponse? ->
                    liveData.setValue(Resource.success(response))
                },
                    { throwable: Throwable? -> liveData.setValue(Resource.error(throwable!!)) })
            )
        }
    }

    private fun isValidInput(topicId: Int): Boolean {
        val validation = FormValidation(activity)

        return if (topicId != 1 && topicId != 2 && topicId != 4) {
            if (!validation.isEmpty(
                    binding.edtTopic.text.toString(),
                    activity.resources.getString(R.string.enter_topic)
                )
            ) {
                binding.edtTopic.setHintTextColor(activity.getColorStateList(R.color.red))
                binding.edtTopic.setBackgroundResource(R.drawable.edt_error_bg)
                binding.edtTopic.requestFocus()
                false
            } else {
                return true
            }
        } else {
            if (!validation.isEmpty(
                    binding.edtTopic.text.toString(),
                    activity.resources.getString(R.string.enter_topic)
                )
            ) {
                binding.edtTopic.setHintTextColor(activity.getColorStateList(R.color.red))
                binding.edtTopic.setBackgroundResource(R.drawable.edt_error_bg)
                binding.edtTopic.requestFocus()
                false
            } else if (activity.preferences.getString(Constants.LANGUAGE)?.isEmpty()!!) {
                Utils.makeToast(activity, activity.getString(R.string.choose_language))
                false
            } else if (activity.preferences.getInt(Constants.topicLength) == 0) {
                Utils.makeToast(activity, activity.getString(R.string.select_length))
                false
            } else {
                return true
            }
        }
    }

    fun initView() {
        val mTextEditorWatcher: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int, count: Int
            ) {
                binding.tvCounter.text =
                    s.length.toString() + activity.getString(R.string.max_count)
                binding.edtTopic.setBackgroundResource(R.drawable.edt_bg)
                binding.edtTopic.setHintTextColor(activity.getColorStateList(R.color.white))
            }

            override fun afterTextChanged(s: Editable) {}
        }
        binding.edtTopic.addTextChangedListener {
            binding.edtTopic.addTextChangedListener(mTextEditorWatcher)
        }
        binding.header.ivDrawer.visibility = View.GONE
        binding.header.tv.visibility = View.GONE
        binding.header.tvTitle.visibility = View.VISIBLE
        binding.header.tvTitle.text = activity.getString(R.string.composition)
        binding.header.ivBack.setOnClickListener {
            activity.onBackPressedDispatcher.onBackPressed()
        }
    }

    fun topicSettingAdapter(topicId: Int) {
        list = ArrayList()
        list.clear()
        list.add(
            TopicSettingModel(
                R.drawable.ic_choose_language,
                activity.getString(R.string.choose_language),
                activity.preferences.getString(Constants.LANGUAGE).toString()
            )
        )
        if (topicId == 1 || topicId == 2 || topicId == 4) {
            list.add(
                TopicSettingModel(
                    R.drawable.ic_select_length,
                    activity.getString(R.string.select_length),
                    activity.preferences.getString(Constants.LENGTH).toString()
                )
            )
        }
        adapter = CommonSettingAdapter(activity, list, onClick = {
            when (it) {
                0 -> {
                    val intent = Intent(activity, ChooseLanguageActivity::class.java)
                    activity.startActivity(intent)
                }

                1 -> {
                    selectLength(topicId)
                }
            }
        })
        binding.rcvSetting.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.rcvSetting.adapter = adapter
    }

    private fun selectLength(topicId: Int) {
        val dialog = BottomSheetDialog(activity, R.style.CustomBottomSheetDialog)
        val view = LayoutInflater.from(activity).inflate(R.layout.length_bottom_sheet, null)
        lengthList = ArrayList()
        lengthList.clear()
        lengthList.add(LengthModel(activity.resources.getString(R.string.small)))
        lengthList.add(LengthModel(activity.resources.getString(R.string.medium)))
        lengthList.add(LengthModel(activity.resources.getString(R.string.large)))
        val btnClose = view.findViewById<AppCompatImageView>(R.id.ivClose)
        val btnSubmit = view.findViewById<AppCompatButton>(R.id.btnSubmit)
        val rcvLength = view.findViewById<RecyclerView>(R.id.rcvSelectLength)
        adapterLength = LengthAdapter(activity, lengthList, positionClick = { position ->
            activity.preferences.putInt(Constants.LENGTH_POSITION, position)
        })
        rcvLength.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rcvLength.adapter = adapterLength
        btnClose.setOnClickListener {
            dialog.dismiss()
        }
        btnSubmit.setOnClickListener {
            activity.preferences.putString(
                Constants.LENGTH, when (activity.preferences.getInt(Constants.LENGTH_POSITION)) {
                    0 -> {
                        activity.resources.getString(R.string.small)
                    }

                    1 -> {
                        activity.resources.getString(R.string.medium)
                    }

                    else -> {
                        activity.resources.getString(R.string.large)
                    }
                }
            )
            activity.preferences.putInt(
                Constants.topicLength,
                when (activity.preferences.getInt(Constants.LENGTH_POSITION)) {
                    0 -> {
                        150
                    }

                    1 -> {
                        500
                    }

                    else -> {
                        1500
                    }
                }
            )
            dialog.dismiss()
            topicSettingAdapter(topicId)
        }
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(view)
        dialog.show()
    }
}