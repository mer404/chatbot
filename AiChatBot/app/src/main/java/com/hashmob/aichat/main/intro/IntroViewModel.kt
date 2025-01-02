package com.hashmob.aichat.main.intro

import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.viewpager2.widget.ViewPager2
import com.hashmob.aichat.R
import com.hashmob.aichat.constants.Constants
import com.hashmob.aichat.data.repository.MainRepository
import com.hashmob.aichat.databinding.ActivityIntroBinding
import com.hashmob.aichat.main.home.ui.HomeActivity
import javax.inject.Inject

class IntroViewModel @Inject constructor(var authRepository: MainRepository) : ViewModel() {
    lateinit var activity: IntroActivity
    lateinit var binding: ActivityIntroBinding

    private var introViewPagerAdapter: IntroViewPagerAdapter? = null
    private lateinit var list: ArrayList<IntroModel>
    fun initialize(activity: IntroActivity, binding: ActivityIntroBinding) {
        this.activity = activity
        this.binding = binding
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.btnGreat -> {
                if (binding.viewpager.currentItem == 1) {
                    activity.preferences.putBoolean(Constants.first_open, true)
                    val intent = Intent(activity, HomeActivity::class.java)
                    activity.startActivity(intent)
                    activity.finish()
                } else {
                    binding.viewpager.currentItem += 1
                }
            }
        }
    }

    fun initView() {
        binding.viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 0) {
                    binding.btnGreat.setText(R.string.great)
                } else {
                    binding.btnGreat.setText(R.string.wonderful)
                }
            }
        })
    }

    fun intro() {
        list = ArrayList()
        list.clear()
        list.add(IntroModel(R.drawable.ic_intro_great, activity.getString(R.string.intro_text_one)))
        list.add(IntroModel(R.drawable.ic_intro_wonderful, activity.getString(R.string.intro_text_two)))
        introViewPagerAdapter = IntroViewPagerAdapter(activity, list)
        binding.viewpager.adapter = introViewPagerAdapter
    }
}
