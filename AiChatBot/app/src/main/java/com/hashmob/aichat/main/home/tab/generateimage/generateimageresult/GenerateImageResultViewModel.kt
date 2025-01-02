package com.hashmob.aichat.main.home.tab.generateimage.generateimageresult

import android.content.Intent
import android.graphics.Bitmap
import android.os.Environment
import android.view.View
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModel
import com.hashmob.aichat.R
import com.hashmob.aichat.constants.Constants
import com.hashmob.aichat.data.repository.MainRepository
import com.hashmob.aichat.databinding.ActivityGenerateImageResultBinding
import com.hashmob.aichat.util.Utils
import com.hashmob.aichat.util.Utils.Companion.getImageUri
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject


class GenerateImageResultViewModel @Inject constructor(var authRepository: MainRepository) :
    ViewModel() {
    lateinit var activity: GenerateImageResultActivity
    lateinit var binding: ActivityGenerateImageResultBinding
    var intent: Intent? = null
    fun initialize(
        activity: GenerateImageResultActivity,
        binding: ActivityGenerateImageResultBinding
    ) {
        this.activity = activity
        this.binding = binding
        activity.loadBanner(activity)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.btnShare -> {
                socialShare()
            }

            R.id.btnDownload -> {
                downloadImage()
            }
        }
    }

    private fun downloadImage() {
        val bitmap = binding.ivResult.drawable.toBitmap()
        var outStream: FileOutputStream? = null
        val dir =
            File(activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.path + Constants.AiChat)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        val fileName = String.format("%d.jpg", System.currentTimeMillis())
        val outFile = File(dir, fileName)
        outStream = FileOutputStream(outFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
        outStream.flush()
        outStream.close()
        Utils.makeToast(activity, activity.getString(R.string.image_download_success))
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

    private fun socialShare() {
        val bitmap: Bitmap = binding.ivResult.drawable.toBitmap()
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/jpeg"
        intent.putExtra(Intent.EXTRA_STREAM, getImageUri(activity, bitmap))
        activity.startActivity(
            Intent.createChooser(
                intent,
                activity.getString(R.string.share_image)
            )
        )
    }
}