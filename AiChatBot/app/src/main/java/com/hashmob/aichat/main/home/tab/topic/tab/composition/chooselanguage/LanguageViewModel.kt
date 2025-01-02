package com.hashmob.aichat.main.home.tab.topic.tab.composition.chooselanguage

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.hashmob.aichat.R
import com.hashmob.aichat.constants.Constants
import com.hashmob.aichat.data.model.firebase.data.LanguageItem
import com.hashmob.aichat.data.repository.MainRepository
import com.hashmob.aichat.databinding.ActivityChooseLanguageBinding
import javax.inject.Inject

class LanguageViewModel @Inject constructor(var authRepository: MainRepository) : ViewModel() {
    lateinit var activity: ChooseLanguageActivity
    lateinit var binding: ActivityChooseLanguageBinding
    private var languageAdapter: LanguageAdapter? = null
    private lateinit var list: ArrayList<LanguageItem>
    fun initialize(activity: ChooseLanguageActivity, binding: ActivityChooseLanguageBinding) {
        this.activity = activity
        this.binding = binding
    }

    fun initView() {
        binding.header.ivDrawer.visibility = View.GONE
        binding.header.tv.visibility = View.GONE
        binding.header.tvTitle.visibility = View.VISIBLE
        binding.header.tvTitle.text = activity.getString(R.string.language)
        binding.header.ivBack.setOnClickListener {
            activity.onBackPressedDispatcher.onBackPressed()
        }
    }

    fun setData() {
        list = ArrayList()
        list.clear()
        for (i in 0 until (activity.preferences.getData().language?.size!!)) {
            list.add(activity.preferences.getData().language?.get(i)!!)
        }
        activity.preferences.getData().language?.let {
            activity.preferences.putString(Constants.LANGUAGE, it[0]?.name)
        }
        languageAdapter = LanguageAdapter(activity, list, onClick = { position, language ->
            LanguageAdapter.selectedPosition = position
            languageAdapter?.notifyDataSetChanged()
            activity.preferences.putString(Constants.LANGUAGE, language)
            activity.preferences.putInt(Constants.LANGUAGE_POSITION,position)
            activity.onBackPressed()
        })
        binding.rcvLanguage.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.rcvLanguage.adapter = languageAdapter
    }

}