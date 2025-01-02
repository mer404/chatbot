package com.hashmob.aichat.main.home.tab.topic.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.GridLayoutManager
import com.hashmob.aichat.R
import com.hashmob.aichat.constants.Constants
import com.hashmob.aichat.data.repository.MainRepository
import com.hashmob.aichat.databinding.ActivityCategoriesChatBinding
import com.hashmob.aichat.main.home.tab.topic.tab.composition.CompositionActivity
import com.hashmob.aichat.main.home.tab.topic.tab.utils.adapter.CategoryAdapter
import com.hashmob.aichat.main.home.tab.topic.tab.utils.model.CategoriesModel
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
class CategoriesChatViewModel @Inject constructor(var authRepository: MainRepository) :
    ViewModel() {
    lateinit var activity: CategoriesChatActivity
    lateinit var binding: ActivityCategoriesChatBinding
    private var categoryAdapter: CategoryAdapter? = null
    private lateinit var categoryList: ArrayList<CategoriesModel>
    fun initialize(activity: CategoriesChatActivity, binding: ActivityCategoriesChatBinding) {
        this.activity = activity
        this.binding = binding
        activity.loadBanner(activity)
    }

    fun initView() {
        binding.header.ivDrawer.visibility = View.GONE
        binding.header.tv.visibility = View.GONE
        binding.header.tvTitle.visibility = View.VISIBLE
        binding.header.tvTitle.text = activity.getString(R.string.categories_chat)
        binding.header.ivBack.setOnClickListener {
            activity.onBackPressedDispatcher.onBackPressed()
        }
    }

    fun category() {
        categoryList = ArrayList()
        categoryList.clear()
        categoryList.add(
            CategoriesModel(
                1,
                R.drawable.ic_write, activity.getString(R.string.write),
                ""
            )
        )
        categoryList.add(
            CategoriesModel(
                2,
                R.drawable.ic_plan,
                activity.getString(R.string.plan),
                activity.getString(R.string.plan_sub_title),
            )
        )
        categoryList.add(
            CategoriesModel(
                3,
                R.drawable.ic_ai_code,
                activity.getString(R.string.ai_code),
                activity.getString(R.string.ai_code_sub_title)
            )
        )
        categoryList.add(
            CategoriesModel(
                4,
                R.drawable.ic_interview,
                activity.getString(R.string.interview),
                activity.getString(R.string.interview_sub_title)
            )
        )
        categoryList.add(
            CategoriesModel(
                5,
                R.drawable.ic_word_on_emoji,
                activity.getString(R.string.word_to_emoji),
                activity.getString(R.string.word_to_emoji_sub_title)
            )
        )
        categoryList.add(
            CategoriesModel(
                6,
                R.drawable.ic_grammer_correct,
                activity.getString(R.string.grammar_correct),
                activity.getString(R.string.grammar_correct_sub_title)
            )
        )
        categoryList.add(
            CategoriesModel(
                7,
                R.drawable.ic_translator,
                activity.getString(R.string.translator),
                activity.getString(R.string.translator_sub_title)
            )
        )
        val layoutManager = GridLayoutManager(activity, 2)
        binding.rcvCategory.layoutManager = layoutManager
        categoryAdapter = CategoryAdapter(activity, categoryList, onClick = {
            onClick(it)
        })
        binding.rcvCategory.adapter = categoryAdapter
    }

    private fun onClick(position: Int) {
        startActivity(
            CompositionActivity(),
            categoryList[position].category,
            categoryList[position].id,
            categoryList[position].subTitle
        )
    }

    private fun startActivity(
        intentActivity: Activity,
        topicName: String,
        settingFlag: Int,
        subTitle: String
    ) {
        val intent = Intent(activity, intentActivity::class.java)
        intent.putExtra(Constants.TOPIC_NAME, topicName)
        intent.putExtra(Constants.SETTING_FLAG, settingFlag)
        intent.putExtra(Constants.SUBTITLE, subTitle)
        activity.startActivity(intent)
    }
}