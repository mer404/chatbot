package com.hashmob.aichat.main.home.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.provider.Settings.Secure
import android.text.SpannableStringBuilder
import android.view.Gravity
import android.view.View
import androidx.annotation.DimenRes
import androidx.core.text.bold
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.hashmob.aichat.BuildConfig
import com.hashmob.aichat.R
import com.hashmob.aichat.constants.Constants
import com.hashmob.aichat.data.repository.MainRepository
import com.hashmob.aichat.databinding.ActivityHomeBinding
import com.hashmob.aichat.main.home.premium.PremiumActivity
import com.hashmob.aichat.main.home.tab.chatbot.ChatActivity
import com.hashmob.aichat.main.home.tab.generateimage.GenerateImageActivity
import com.hashmob.aichat.main.home.tab.scanmathsolving.ScanMathSolvingActivity
import com.hashmob.aichat.main.home.tab.topic.ui.CategoriesChatActivity
import com.hashmob.aichat.main.home.ui.drawer.BlurBuilder
import com.hashmob.aichat.util.GlobalMethods
import com.hashmob.aichat.util.LogUtils
import com.hashmob.aichat.util.PremiumUtils
import com.hashmob.aichat.util.Utils
import com.hashmob.aichat.util.Utils.Companion.TAG
import com.hashmob.aichat.util.Utils.Companion.createBitmapFromLayout
import com.hashmob.aichat.util.Utils.Companion.rateUs
import com.hashmob.aichat.util.Utils.Companion.shareApp
import com.hashmob.aichat.util.Utils.Companion.termsPrivacy
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject
import kotlin.math.abs


@SuppressLint("StaticFieldLeak")
class HomeViewModel @Inject constructor(var authRepository: MainRepository) : ViewModel() {
    lateinit var activity: HomeActivity
    lateinit var binding: ActivityHomeBinding
    private lateinit var list: ArrayList<ViewPagerModel>
    private var viewPagerAdapter: ViewPagerAdapter? = null
    private var adCounter = 0
    private var android_id=""

    fun initialize(activity: HomeActivity, binding: ActivityHomeBinding) {
        this.activity = activity
        this.binding = binding
        activity.loadBanner(activity)
        activity.interstitialAds()
        PremiumUtils.checkSubscriptionStatus(activity, this::checkState)
        android_id = Secure.getString(
            activity.getContentResolver(),
            Secure.ANDROID_ID)
        LogUtils.Print(TAG, "android_id: $android_id")
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.premiumContainer -> {
                startActivity(PremiumActivity())
            }
        }
    }

    fun setAdapter() {
        list = ArrayList()
        list.clear()
        list.add(
            ViewPagerModel(
                R.drawable.ic_generate_iv,
                activity.getString(R.string.generate_image),
                activity.getString(R.string.turn_prompts_into_art_using_ai)
            )
        )
        list.add(
            ViewPagerModel(
                R.drawable.ic_topic,
                activity.getString(R.string.topic),
                activity.getString(R.string.topic_description)
            )
        )
        list.add(
            ViewPagerModel(
                R.drawable.ic_chat_with_ai,
                activity.getString(R.string.chat_with_ai_title),
                activity.getString(R.string.chat_with_ai)
            )
        )
        list.add(
            ViewPagerModel(
                R.drawable.ic_math_solving,
                activity.getString(R.string.scan_math_solving),
                activity.getString(R.string.scan_anything_and_get_answer)
            )
        )
        viewPagerAdapter = ViewPagerAdapter(activity, list, onClick = { position ->
            when (position) {
                0 -> {
                    startActivity(GenerateImageActivity())
                }

                1 -> {
                    startActivity(CategoriesChatActivity())
                }

                2 -> {
                    startActivity(ChatActivity())
                }

                3 -> {
                    startActivity(ScanMathSolvingActivity())
                }
            }
            if (adCounter == activity.preferences.getRemoteConfig()?.adsPresentCount) {
                if (activity.preferences.isProVersion()) return@ViewPagerAdapter
                activity.interstitialAd?.show(activity)
                activity.interstitialAds()
                adCounter = 0
            } else {
                adCounter++
            }
        })
        binding.viewpager.adapter = viewPagerAdapter
        binding.viewpager.offscreenPageLimit = 1
        val nextItemVisiblePx = activity.resources.getDimension(R.dimen.viewpager_next_item_visible)
        val currentItemHorizontalMarginPx =
            activity.resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin)
        val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
        val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
            page.translationX = -pageTranslationX * position
            //Make selected item bigger than other tabs
            page.scaleY = 1 - (0.25f * abs(position))
        }
        binding.viewpager.setPageTransformer(pageTransformer)
        val itemDecoration = HorizontalMarginItemDecoration(
            activity, R.dimen.viewpager_current_item_horizontal_margin
        )
        binding.viewpager.addItemDecoration(itemDecoration)
        binding.dotsIndicator.attachTo(binding.viewpager)
    }

    fun initView() {
        val bitmap: Bitmap = createBitmapFromLayout(binding.container)!!
        val blurredBitmap: Bitmap = BlurBuilder.blur(activity, bitmap)
        binding.drawerLayout.ivBlur.background = (BitmapDrawable(activity.resources, blurredBitmap))

        binding.drawerLayout.tvVersion.text =
            activity.getString(R.string.version) + " " + BuildConfig.VERSION_NAME
        binding.drawerLayout.ivCloseDrawer.setOnClickListener {
            binding.drawer.closeDrawers()
        }
        binding.drawerLayout.tvHome.setOnClickListener {
            binding.drawer.closeDrawers()
        }
        binding.drawerLayout.tvRestorePurchases.setOnClickListener {
            startActivity(PremiumActivity())
            binding.drawer.closeDrawers()
        }
        binding.drawerLayout.tvHelp.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            val recipients = arrayOf(activity.preferences.getRemoteConfig()?.contactUsEmail)
            intent.putExtra(Intent.EXTRA_EMAIL, recipients)
            intent.putExtra(Intent.EXTRA_SUBJECT, activity.getString(R.string.app_name))
            intent.putExtra(
                Intent.EXTRA_TEXT,
                GlobalMethods.deviceName + "\n" + android.os.Build.VERSION.RELEASE
            )
            intent.type = "text/html"
            intent.setPackage("com.google.android.gm")
            activity.startActivity(Intent.createChooser(intent, ""))
            binding.drawer.closeDrawers()
        }
        binding.drawerLayout.tvShare.setOnClickListener {
            shareApp(activity)
        }
        binding.drawerLayout.tvTerms.setOnClickListener {
            termsPrivacy(activity, 1)
            binding.drawer.closeDrawers()
        }
        binding.drawerLayout.tvPrivacy.setOnClickListener {
            termsPrivacy(activity, 0)
            binding.drawer.closeDrawers()
        }
        binding.drawerLayout.tvRateUs.setOnClickListener {
            rateUs(activity)
            binding.drawer.closeDrawers()
        }
        binding.header.ivDrawer.setOnClickListener {
            binding.drawer.openDrawer(Gravity.LEFT)
        }
        binding.header.ivBack.visibility = View.GONE
        binding.header.ivDrawer.visibility = View.VISIBLE
        val s =
            SpannableStringBuilder().bold { append(activity.resources.getString(R.string.chat_bot_has_sent_you_a_premium_gift)) }
                .append(activity.resources.getString(R.string.tap_to_receive))
        binding.txtPremium.text = s
        currentDateForTrialCounter()
        activity.preferences.getString(Constants.LANGUAGE).toString().ifEmpty {
            activity.preferences.putString(
                Constants.LANGUAGE,
                activity.resources.getString(R.string.english)
            )
        }.toString()
        activity.preferences.getString(Constants.LENGTH).toString().ifEmpty {
            activity.preferences.putInt(Constants.topicLength, 150)
            activity.preferences.putString(
                Constants.LENGTH,
                activity.resources.getString(R.string.small)
            )
        }.toString()
    }

    private fun currentDateForTrialCounter() {
        val sdf = SimpleDateFormat(Constants.DATE_YYYY_MM_DD_FORMAT)
        val currentDateAndTime = sdf.format(Date())
        if (currentDateAndTime != activity.preferences.getString(Constants.DATE_YYYY_MM_DD_FORMAT)) {
            activity.preferences.putInt(
                Constants.IMAGE_COUNTER,
                activity.preferences.getRemoteConfig()?.freeCount
            )
            activity.preferences.putInt(
                Constants.PRO_IMAGE_COUNTER,
                activity.preferences.getRemoteConfig()?.adsPresentCount
            )
        }
        activity.preferences.putString(Constants.DATE_YYYY_MM_DD_FORMAT, currentDateAndTime)
    }

    inner class HorizontalMarginItemDecoration(
        context: Context, @DimenRes horizontalMarginInDp: Int
    ) : RecyclerView.ItemDecoration() {

        private val horizontalMarginInPx: Int =
            context.resources.getDimension(horizontalMarginInDp).toInt()

        override fun getItemOffsets(
            outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
        ) {
            outRect.right = horizontalMarginInPx
            outRect.left = horizontalMarginInPx
        }

    }

    private fun startActivity(intentActivity: Activity) {
        val intent = Intent(activity, intentActivity::class.java)
        activity.startActivity(intent)
    }

    private fun checkState(status: Int, message: String) {
        when (status) {
            Constants.PURCHASED -> {
                activity.preferences.putInt(
                    Constants.isProVersion, Constants.pro_version
                )
                activity.preferences.putInt(
                    Constants.PRO_IMAGE_COUNTER,
                    activity.preferences.getRemoteConfig()?.adsPresentCount
                )
            }

            Constants.NOT_PURCHASED -> {
                activity.preferences.putInt(
                    Constants.isProVersion, Constants.not_pro_version
                )
                activity.preferences.putInt(
                    Constants.PRO_IMAGE_COUNTER,
                    activity.preferences.getRemoteConfig()?.freeCount
                )
            }

            Constants.ERR_MESSAGE -> {
                Utils.makeToast(activity, message)
            }
        }
    }
}