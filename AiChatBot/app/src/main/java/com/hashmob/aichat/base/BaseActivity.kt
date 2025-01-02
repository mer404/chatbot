package com.hashmob.aichat.base


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import androidx.annotation.IdRes
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.hashmob.aichat.R
import com.hashmob.aichat.constants.Constants
import com.hashmob.aichat.util.ImageLoaderUtils
import com.hashmob.aichat.util.LogUtils
import com.hashmob.aichat.util.Preferences
import com.hashmob.aichat.util.ProgressDialog
import com.hashmob.aichat.util.Utils
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_home.adViewContainer
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject


abstract class BaseActivity : AppCompatActivity() {

    @Inject
    lateinit var preferences: Preferences
    protected abstract fun initViewBinding()
    var interstitialAd: InterstitialAd? = null
    var adView: AdView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        preferences = Preferences(this)
        initViewBinding()
        adView = AdView(baseContext)
    }

    open fun showProgress() {
        ProgressDialog.instance?.show(this)
    }

    open fun hideProgress() {
        ProgressDialog.instance?.dismiss()
    }


    fun replaceFragment(
        fragment: Fragment,
        backStackName: Boolean = false,
        popStack: Boolean = false,
        aTAG: String = "",
        @IdRes containerViewId: Int = R.id.container
    ) {


        val transition = supportFragmentManager
            .beginTransaction()
        transition.setCustomAnimations(
            R.anim.slide_in_from_right,
            R.anim.slide_out_from_left,
            R.anim.slide_in_from_left,
            R.anim.slide_out_from_right
        )
        if (popStack)
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        if (backStackName)
            transition.addToBackStack(aTAG)

        transition.replace(containerViewId, fragment).commit()

    }

    fun addFragment(
        @NonNull fragment: Fragment,
        backStackName: Boolean = false,
        aTAG: String = "",
        @IdRes containerViewId: Int = R.id.container
    ) {

        val transition = supportFragmentManager.beginTransaction()
        if (backStackName)
            transition.addToBackStack(aTAG)
        transition.setCustomAnimations(
            R.anim.slide_in_from_right,
            R.anim.slide_out_from_left,
            R.anim.slide_in_from_left,
            R.anim.slide_out_from_right
        )

        transition.add(containerViewId, fragment).commit()
    }

    fun isFragmentAlreadyInStack(tag1: String): Boolean {
        if (supportFragmentManager.backStackEntryCount > 0) {
            val backEntry =
                supportFragmentManager.getBackStackEntryAt(supportFragmentManager.backStackEntryCount - 1)
            val tag = backEntry.name

            return (tag.equals(tag1))
        } else {
            return false
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun setAllPermission() {
        val requiredPermissions =
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
            )
        val missingPermissions = requiredPermissions.filter { permission ->
            checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED
        }

        if (missingPermissions.isEmpty()) {
//            locationEnabled()
        } else {
            TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage(getString(R.string.if_you_reject_permission_you_can_not_use_this_service_please_turn_on_permissions_at_setting_permission))
                .setPermissions(
                    Manifest.permission.CAMERA
                )
                .check()
        }

    }

    var permissionlistener: PermissionListener = object : PermissionListener {
        override fun onPermissionGranted() {
            LogUtils.Print("TAGS___", "PERMISSION_GRANTED")
        }

        @RequiresApi(Build.VERSION_CODES.M)
        override fun onPermissionDenied(deniedPermissions: List<String>) {
            LogUtils.Print("TAGS___", "PERMISSION_DENIED")
        }
    }

    //on activity result
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Constants.REQUEST_CODE_CAMERA_PERMISSION) {
            LogUtils.Print("TAGS___", "PERMISSION_GRANTED___RESULT")
        }

    }

    companion object {

        /**
         * SET DATA TO VIEW
         */
        fun setDataToViewNoTransform(
            context: Context?,
            strImage: String?,
            imageView: ImageView,
            intPlaceholder: Int
        ) {
            if (strImage != null && strImage != "") {
                ImageLoaderUtils.loadImageFromUrlWithoutResizeViaGlide(
                    context,
                    strImage,
                    imageView,
                    intPlaceholder,
                    RequestOptions.noTransformation()
                )
            }
        }


        /**
         * SET DATA TO VIEW
         */
        fun setDataToView(
            context: Context?,
            strImage: String?,
            imageView: ImageView,
            intPlaceholder: Int
        ) {
            if (strImage != null && strImage != "") {
                ImageLoaderUtils.loadImageFromUrlWithoutResizeViaGlide(
                    context,
                    strImage,
                    imageView,
                    intPlaceholder,
                    RequestOptions.centerCropTransform()
                )
            }
        }

        fun getDate(date: String): String {
            if (date != null) {
                val fmt = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
                fmt.timeZone = TimeZone.getTimeZone("UTC")
                val outputFormat = SimpleDateFormat("hh:mm", Locale.US)
                outputFormat.timeZone = TimeZone.getDefault()
//            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                val date = fmt.parse(date)
                val formattedDate = outputFormat.format(date!!)

                return formattedDate.toString()
            } else {
                return ""
            }
        }

        fun getDateFull(date: String): String {
            if (date != null) {
                val fmt = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
                fmt.timeZone = TimeZone.getTimeZone("UTC")
                val outputFormat = SimpleDateFormat("MMMM d, yyyy", Locale.US)
//                val outputFormat = SimpleDateFormat("MMMM d, yyyy hh:mm:ss a", Locale.US)
                outputFormat.timeZone = TimeZone.getDefault()
//            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                val date = fmt.parse(date)
                val formattedDate = outputFormat.format(date!!)

                return formattedDate.toString()
            } else {
                return ""
            }
        }
    }

    open fun startWithClearStack(activity: Class<out Activity?>?) {
        startActivity(
            Intent(
                this,
                activity
            ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        )
        finish()
    }
    fun interstitialAds() {
        if (preferences.isProVersion()) return
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(baseContext,
            preferences.getRemoteConfig()?.androidInterstitalAds.toString(),
            adRequest,
            object : InterstitialAdLoadCallback() {

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    interstitialAd = null
                }

                override fun onAdLoaded(interstitialAds: InterstitialAd) {
                    interstitialAd = interstitialAds
                }
            })
    }
    fun loadBanner(activity: Activity) {
        if (preferences.isProVersion()) return
        adView = AdView(activity)
        adView!!.adUnitId = preferences.getRemoteConfig()?.androidBannerAds.toString()
        adViewContainer.removeAllViews()
        adViewContainer.addView(adView)
        val adSize = Utils.getAdSize(activity)
        adView!!.setAdSize(adSize)
        val adRequest = AdRequest.Builder().build()
        adView!!.loadAd(adRequest)
    }
  }