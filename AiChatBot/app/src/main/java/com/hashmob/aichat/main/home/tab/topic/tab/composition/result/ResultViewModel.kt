package com.hashmob.aichat.main.home.tab.topic.tab.composition.result

import android.content.ClipboardManager
import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.text.method.ScrollingMovementMethod
import android.view.View
import androidx.lifecycle.ViewModel
import com.hashmob.aichat.data.repository.MainRepository
import com.hashmob.aichat.databinding.ActivityCompositionResultBinding
import com.hashmob.aichat.util.Utils
import java.util.Locale
import javax.inject.Inject


class ResultViewModel @Inject constructor(var authRepository: MainRepository) : ViewModel(),
    TextToSpeech.OnInitListener {
    lateinit var activity: CompositionResultActivity
    lateinit var binding: ActivityCompositionResultBinding
    var tts: TextToSpeech? = null
    fun initialize(activity: CompositionResultActivity, binding: ActivityCompositionResultBinding) {
        this.activity = activity
        this.binding = binding
        tts = TextToSpeech(activity, this)
        activity.loadBanner(activity)
    }

    fun onClick(view: View) {
        when (view.id) {
            com.hashmob.aichat.R.id.btnCopy -> {
                val clipManager: ClipboardManager =
                    activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipManager.text = binding.tvResult.text
                binding.btnCopy.text =
                    activity.resources.getString(com.hashmob.aichat.R.string.copied)
                Utils.makeToast(
                    activity,
                    activity.getString(com.hashmob.aichat.R.string.copied_to_clipboard)
                )
            }

            com.hashmob.aichat.R.id.btnListen -> {
                speakOut()
            }

            com.hashmob.aichat.R.id.btnStopListen -> {
                if (tts?.isSpeaking!!) {
                    tts!!.stop()
                    binding.btnStopListen.visibility = View.INVISIBLE
                    binding.btnListen.visibility = View.VISIBLE
                }
            }
        }
    }

    fun initView() {
        binding.tvResult.movementMethod = ScrollingMovementMethod()
        binding.header.ivPdf.visibility = View.VISIBLE
        binding.header.tv.visibility = View.GONE
        binding.header.tvTitle.visibility = View.VISIBLE
        binding.header.tvTitle.text = activity.getString(com.hashmob.aichat.R.string.composition)
        binding.header.ivBack.setOnClickListener {
            activity.onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onInit(status: Int) {
        tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(p0: String?) {
                binding.btnListen.visibility = View.INVISIBLE
                binding.btnStopListen.visibility = View.VISIBLE
            }

            override fun onDone(p0: String?) {
                binding.btnStopListen.visibility = View.INVISIBLE
                binding.btnListen.visibility = View.VISIBLE
            }

            override fun onError(p0: String?) {
            }

//            override fun onRangeStart(utteranceId: String?, start: Int, end: Int, frame: Int) {
//                super.onRangeStart(utteranceId, start, end, frame)
//                activity.runOnUiThread(Runnable {
//                    val textWithHighlights: Spannable = SpannableString(utteranceId)
//                    textWithHighlights.setSpan(
//                        ForegroundColorSpan(Color.YELLOW),
//                        start,
//                        end,
//                        Spanned.SPAN_INCLUSIVE_INCLUSIVE
//                    )
//                    binding.tvResult.text = textWithHighlights
//                })

//            }
        })
        if (status == TextToSpeech.SUCCESS) {
            val result = tts!!.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Utils.makeToast(
                    activity,
                    activity.getString(com.hashmob.aichat.R.string.language_not_supported)
                )
            } else {
                binding.btnListen.isEnabled = true
            }
        } else {
            Utils.makeToast(
                activity,
                activity.getString(com.hashmob.aichat.R.string.initialization_failed)
            )
        }
    }

    private fun speakOut() {
        val text = binding.tvResult.text.toString()
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }
}
