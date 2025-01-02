package com.hashmob.aichat.main.home.tab.scanmathsolving.solution

import android.annotation.SuppressLint
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.ads.AdView
import com.hashmob.aichat.R
import com.hashmob.aichat.data.main.Resource
import com.hashmob.aichat.data.model.apimodel.ChatWithAiResponse
import com.hashmob.aichat.data.repository.MainRepository
import com.hashmob.aichat.databinding.ActivitySolutionBinding
import com.hashmob.aichat.main.home.tab.scanmathsolving.ScanMathSolvingActivity
import com.hashmob.aichat.util.Utils
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class SolutionViewModel @Inject constructor(var authRepository: MainRepository) : ViewModel() {
    @SuppressLint("StaticFieldLeak")
    lateinit var activity: SolutionActivity
    lateinit var binding: ActivitySolutionBinding
    var adView: AdView? = null

    //LIVE DATA
    private val liveData = MutableLiveData<Resource<ChatWithAiResponse>>()
    var prompt: MutableLiveData<String> = MutableLiveData()

    //API
    private val compositeDisposable = CompositeDisposable()
    fun initialize(
        solutionActivity: SolutionActivity, binding: ActivitySolutionBinding
    ) {
        this.activity = solutionActivity
        this.binding = binding
        initView()
    }

    private fun initView() {
        binding.header.ivDrawer.visibility = View.GONE
        binding.header.tv.visibility = View.GONE
        binding.header.tvTitle.visibility = View.VISIBLE
        binding.header.tvTitle.text = activity.getString(R.string.solution)
        binding.header.ivBack.setOnClickListener {
            activity.onBackPressedDispatcher.onBackPressed()
        }
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.btnCopyAnswer -> {
                val clipManager: ClipboardManager =
                    activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipManager.text = binding.txtAnswer.text.toString()
                Utils.makeToast(activity, activity.getString(R.string.copied_to_clipboard))
                binding.btnCopyAnswer.background =
                    activity.resources.getDrawable(R.drawable.btn_copied_bg)
                binding.btnCopyAnswer.text = activity.resources.getString(R.string.copied)
            }

            R.id.btnScanNewQuestion -> {
                val intent = Intent(activity, ScanMathSolvingActivity::class.java)
                activity.startActivity(intent)
                activity.finish()
            }
        }
    }
}