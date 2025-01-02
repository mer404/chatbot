package com.hashmob.aichat.main.home.tab.generateimage

import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.hashmob.aichat.R
import com.hashmob.aichat.constants.Constants
import com.hashmob.aichat.data.main.Resource
import com.hashmob.aichat.data.model.apimodel.GenerateImageResponse
import com.hashmob.aichat.data.model.firebase.data.PromptItem
import com.hashmob.aichat.data.model.firebase.data.StyleItem
import com.hashmob.aichat.data.repository.MainRepository
import com.hashmob.aichat.databinding.ActivityGenerateImageBinding
import com.hashmob.aichat.implementor.PromptInterface
import com.hashmob.aichat.main.home.tab.generateimage.adapter.ArtStyleAdapter
import com.hashmob.aichat.main.home.tab.generateimage.adapter.PromptAdapter
import com.hashmob.aichat.util.FormValidation
import com.hashmob.aichat.util.Utils
import com.hashmob.aichat.util.Utils.Companion.premiumScreenIntent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class GenerateImageViewModel @Inject constructor(var authRepository: MainRepository) : ViewModel() {
    lateinit var activity: GenerateImageActivity
    lateinit var binding: ActivityGenerateImageBinding
    private var promptAdapter: PromptAdapter? = null
    private var artStyleAdapter: ArtStyleAdapter? = null
    private lateinit var promptList: ArrayList<PromptItem>
    private lateinit var artStyleList: ArrayList<StyleItem>

    //LIVE DATA
    private val liveData = MutableLiveData<Resource<GenerateImageResponse>>()
    var prompt: MutableLiveData<String> = MutableLiveData()

    //API
    private val compositeDisposable = CompositeDisposable()
    fun initialize(activity: GenerateImageActivity, binding: ActivityGenerateImageBinding) {
        this.activity = activity
        this.binding = binding
        activity.interstitialAds()
        initView()
    }

    fun getGenerateImageLiveData(): MutableLiveData<Resource<GenerateImageResponse>> {
        return liveData
    }

    private fun callGenerateImageApi() {
        if (isValidInput()) {
            val params = HashMap<String, Any>()
            params[Constants.prompt] = prompt.value.toString()
            params[Constants.n] = 1
            params[Constants.size] = activity.getString(R.string.image_size)
            compositeDisposable.add(authRepository.createImage(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    liveData.setValue(Resource.loading())
                }
                .subscribe(
                    { response: GenerateImageResponse? ->
                        liveData.setValue(Resource.success(response))
                    },
                    { throwable: Throwable? ->
                        liveData.setValue(Resource.error(throwable!!))
                    }
                ))
        }
    }

    private fun isValidInput(): Boolean {
        val validation = FormValidation(activity)
        return if (!validation.isEmpty(
                binding.edtPrompt.text.toString(),
                activity.resources.getString(R.string.enter_prompt)
            )
        ) {
            binding.edtPrompt.setBackgroundResource(R.drawable.btn_premium_error_bg)
            binding.edtPrompt.requestFocus()
            false
        } else {
            true
        }

    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.tvGetPremium -> {
                premiumScreenIntent(activity)
            }

            R.id.btnCreateArt -> {
//                if (activity.preferences.isProVersion()) {
//                    if (activity.preferences.getInt(Constants.PRO_IMAGE_COUNTER) > 0) {
//                        callGenerateImageApi()
//                    } else {
//                        Utils.makeToast(
//                            activity,
//                            activity.getString(R.string.your_daily_limit_has_been_finished)
//                        )
//                    }
//                } else {
//                    if (activity.preferences.getInt(Constants.IMAGE_COUNTER) > 0) {
//                        callGenerateImageApi()
//                    } else {
//                        premiumScreenIntent(activity)
//                    }
//                }
                if (activity.preferences.isProVersion()) {
                    val sdf = SimpleDateFormat(Constants.DATE_YYYY_MM_DD_FORMAT)
                    val currentDateAndTime = sdf.format(Date())

                    var date = Date(activity.preferences.getLong(Constants.firstTimeUsedTimestamp)); // *1000 is to convert seconds to milliseconds
                    var sdf1 = SimpleDateFormat(Constants.DATE_YYYY_MM_DD_FORMAT);
                    val pruDateAndTime = sdf1.format(date)

                    if (currentDateAndTime == pruDateAndTime) {
                        if (activity.preferences.getInt(Constants.generateImageService) < 10) {
                            activity.preferences.putInt("cont",1)
                            callGenerateImageApi()
                        } else {
                            Utils.makeToast(activity, activity.getString(R.string.your_daily_limit_has_been_finished))
                        }
                    }else{
                        val timestamp = Date().time
                        activity.preferences.putInt(Constants.generateImageService, 0)
                        activity.preferences.putLong(Constants.firstTimeUsedTimestamp, timestamp)
                        activity.preferences.putInt("cont",2)
                        callGenerateImageApi()
                    }
                } else {
                    if (activity.preferences.getInt(Constants.allService) < 5) {
                        callGenerateImageApi()
                    } else {
                        premiumScreenIntent(activity)
                    }
                }
            }
        }
    }


    fun initView() {
        binding.header.ivDrawer.visibility = View.GONE
        binding.header.tv.visibility = View.GONE
        binding.header.tvTitle.visibility = View.VISIBLE
        binding.header.tvTitle.setText(R.string.generate_image)
        binding.header.ivBack.setOnClickListener {
            activity.onBackPressedDispatcher.onBackPressed()
        }
    }

    fun searchView() {
        binding.edtPrompt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(newText: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(newText: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.edtPrompt.setBackgroundResource(R.drawable.btn_premium_bg)
            }

            override fun afterTextChanged(text: Editable?) {
//                promptFilter(text.toString())
            }
        })
    }

    private fun promptFilter(text: String) {
        val filterPrompt: ArrayList<PromptItem> = ArrayList()
        for (s in promptList) {
            if (s.prompttext?.lowercase(Locale.getDefault())
                    ?.contains(text.lowercase(Locale.getDefault()))!!
            ) {
                filterPrompt.add(s)
            }
        }
        promptAdapter?.filterList(filterPrompt)
    }

    fun artStyle() {
        artStyleList = ArrayList()
        artStyleList.clear()
        for (i in 0 until (activity.preferences.getData().style?.size!!)) {
            artStyleList.add(activity.preferences.getData().style?.get(i)!!)
        }
        artStyleAdapter = ArtStyleAdapter(activity, artStyleList)
        binding.rcvArtStyle.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding.rcvArtStyle.adapter = artStyleAdapter
    }

    fun prompt() {
        promptList = ArrayList()
        promptList.clear()
        for (i in 0 until (activity.preferences.getData().prompt?.size!!)) {
            promptList.add(activity.preferences.getData().prompt?.get(i)!!)
        }
        promptAdapter = PromptAdapter(activity, promptList, listener)
        binding.rcvPrompt.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding.rcvPrompt.adapter = promptAdapter
    }

    var listener = object : PromptInterface {
        override fun onClick(position: Int) {
            if (!promptAdapter?.list?.get(position)?.flag!!) {
                binding.edtPrompt.setText(promptAdapter!!.list[position].prompttext)
                PromptAdapter.selectedPosition = position
            }
        }
    }
}