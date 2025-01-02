package com.hashmob.aichat.main.home.tab.generateimage.generateimageresult

import android.graphics.drawable.Drawable
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.hashmob.aichat.base.BaseActivity
import com.hashmob.aichat.constants.Constants
import com.hashmob.aichat.databinding.ActivityGenerateImageResultBinding
import com.hashmob.aichat.di.ViewModelFactory
import javax.inject.Inject


class GenerateImageResultActivity : BaseActivity() {
    lateinit var binding: ActivityGenerateImageResultBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var viewModel: GenerateImageResultViewModel

    override fun initViewBinding() {
        binding = ActivityGenerateImageResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = viewModelFactory.create(GenerateImageResultViewModel::class.java)
        binding.lifecycleOwner = this
        viewModel.initialize(this, binding)
        binding.viewModel = viewModel
        setAllPermission()
        viewModel.initView()
        setResultImage()
    }

    private fun setResultImage() {
        val imgUrl = intent.getStringExtra(Constants.url)
        val circularProgressDrawable = CircularProgressDrawable(this)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()
        Glide
            .with(this)
            .load(imgUrl)
            .centerCrop()
            .placeholder(circularProgressDrawable)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .transition(DrawableTransitionOptions.withCrossFade())
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable?>,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    p0: Drawable?,
                    p1: Any?,
                    p2: Target<Drawable?>?,
                    p3: DataSource?,
                    p4: Boolean
                ): Boolean {
                    circularProgressDrawable.stop()
                    return false
                }
            })
            .into(binding.ivResult)
    }
}