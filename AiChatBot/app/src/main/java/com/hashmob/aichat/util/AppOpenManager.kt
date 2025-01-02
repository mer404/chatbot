package com.hashmob.aichat.util

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback
import com.hashmob.aichat.base.BaseApplication
import com.hashmob.aichat.base.BaseApplication.Companion.isOpenAdLoader
import com.hashmob.aichat.base.BaseApplication.Companion.preferences
import java.util.Date

/**
 * Prefetches App Open Ads.
 */
class AppOpenManager(myApplication: BaseApplication) : LifecycleObserver,
    Application.ActivityLifecycleCallbacks {
    private val myApplication: BaseApplication
    var appOpenAd: AppOpenAd? = null
    private lateinit var loadCallback: AppOpenAdLoadCallback
    private var currentActivity: Activity? = null
    private var loadTime: Long = 0

    init {
        this.myApplication = myApplication
        this.myApplication.registerActivityLifecycleCallbacks(this)
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this)
    }

    private lateinit var diff: DefaultLifecycleObserver

    /**
     * Shows the ad if one isn't already showing.
     */
    fun showAdIfAvailable() {
        // Only show ad if there is not already an app open ad currently showing
        // and an ad is available.
        if (!isShowingAd && isAdAvailable()) {
            if (preferences.isProVersion()) return
            LogUtils.Print(TAG, "Will show ad.")
            val fullScreenContentCallback: FullScreenContentCallback =
                object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        LogUtils.Print(TAG, "onAdDismissedFullScreenContent")
                        // Set the reference to null so isAdAvailable() returns false.
                        appOpenAd = null
                        isShowingAd = false
                        fetchAd()
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        LogUtils.Print(TAG, "onAdFailedToShowFullScreenContent")
                    }

                    override fun onAdShowedFullScreenContent() {
                        LogUtils.Print(TAG, "onAdShowedFullScreenContent")
                        isShowingAd = true
                        isOpenAdLoader = true
                    }
                }
            appOpenAd?.fullScreenContentCallback = fullScreenContentCallback
            appOpenAd?.show(currentActivity!!)
        } else {
            LogUtils.Print(TAG, "Can not show ad.")
            fetchAd()
        }
    }

    /**
     * LifecycleObserver methods
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        showAdIfAvailable()
        LogUtils.Print(TAG, "onStart")
    }

    /**
     * Request an ad
     */
    fun fetchAd() {
        if (preferences.isProVersion()) return
        // Have unused ad, no need to fetch another.
        if (isAdAvailable()) {
            return
        }
        loadCallback = object : AppOpenAdLoadCallback() {
            override fun onAdLoaded(ad: AppOpenAd) {
                LogUtils.Print(TAG, "onAdLoaded")
                appOpenAd = ad
                loadTime = Date().time
                appOpenAd?.show(currentActivity!!)
                showAdIfAvailable()
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                // Handle the error.
                LogUtils.Print(TAG, "failed to load")
            }
        }
        val request: AdRequest = adRequest()
        if (!preferences.isProVersion()) {
            AppOpenAd.load(
                myApplication,
                preferences.getRemoteConfig()?.androidAppOpenAds.toString(),
                request,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
                loadCallback
            )
        }
    }

    /**
     * Creates and returns ad request.
     */
    private fun adRequest(): AdRequest {
        return AdRequest.Builder().build()
    }

    /**
     * Utility method to check if ad was loaded more than n hours ago.
     */
    private fun wasLoadTimeLessThanNHoursAgo(numHours: Long): Boolean {
        LogUtils.Print(TAG, "wasLoadTimeLessThanNHoursAgo")
        val dateDifference = Date().time - loadTime
        val numMilliSecondsPerHour: Long = 3600000
        return dateDifference < numMilliSecondsPerHour * numHours
    }// && wasLoadTimeLessThanNHoursAgo(1);

    /**
     * Utility method that checks if ad exists and can be shown.
     */
    fun isAdAvailable(): Boolean {
        LogUtils.Print(TAG, "isAdAvailable")
        return appOpenAd != null // && wasLoadTimeLessThanNHoursAgo(1);
    }


    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        LogUtils.Print(TAG, "onActivityCreated")
    }

    override fun onActivityStarted(activity: Activity) {
        LogUtils.Print(TAG, "onActivityStarted")
        currentActivity = activity
    }

    override fun onActivityResumed(activity: Activity) {
        LogUtils.Print(TAG, "onActivityResumed")
        currentActivity = activity
    }

    override fun onActivityPaused(activity: Activity) {
        LogUtils.Print(TAG, "onActivityPaused")
    }

    override fun onActivityStopped(activity: Activity) {
        LogUtils.Print(TAG, "onActivityStopped")
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        LogUtils.Print(TAG, "onActivitySaveInstanceState")
    }

    override fun onActivityDestroyed(activity: Activity) {
        LogUtils.Print(TAG, "onActivityDestroyed")
        currentActivity = null
    }

    companion object {
        private const val TAG = "AppOpenManager"
        var isShowingAd = false
    }
}