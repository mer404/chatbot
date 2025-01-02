package com.hashmob.aichat.main.splash

import android.content.Intent
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.provider.Settings.Secure
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.gson.GsonBuilder
import com.hashmob.aichat.BuildConfig
import com.hashmob.aichat.R
import com.hashmob.aichat.base.BaseActivity
import com.hashmob.aichat.base.BaseApplication.Companion.appOpenManager
import com.hashmob.aichat.base.BaseApplication.Companion.isOpenAdLoader
import com.hashmob.aichat.constants.Constants
import com.hashmob.aichat.constants.Constants.needToWait
import com.hashmob.aichat.data.model.firebase.data.GenerateImageService
import com.hashmob.aichat.data.model.firebase.data.Response
import com.hashmob.aichat.data.model.firebase.data.all_service
import com.hashmob.aichat.data.model.firebase.remoteconfig.RemoteConfigData
import com.hashmob.aichat.databinding.ActivitySplashBinding
import com.hashmob.aichat.main.home.ui.HomeActivity
import com.hashmob.aichat.main.intro.IntroActivity
import com.hashmob.aichat.util.LogUtils
import com.hashmob.aichat.util.PremiumUtils
import com.hashmob.aichat.util.Utils
import com.onesignal.OneSignal


class SplashActivity : BaseActivity() {
    val TAG = "SplashActivity"
    private lateinit var binding: ActivitySplashBinding
    lateinit var view: View
    private var isBack = false
    private var android_id=""
    var database: DatabaseReference = Firebase.database.reference
    override fun initViewBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        getRemoteConfigSettings()
        PremiumUtils.checkSubscriptionStatus(this, this::checkState)
        android_id = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID)
    }

    /**
     * GET REMOTE CONFIG SETTINGS
     */
    private fun getRemoteConfigSettings() {
        if (Utils.isInternetOn(this)) {
            val mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
            val configSettings = FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(0).build()
            mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings)
            mFirebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val updated = task.result
                    val value =
                        mFirebaseRemoteConfig.getString(if (BuildConfig.DEBUG) Constants.config_test else Constants.config)
                    LogUtils.Print(TAG, "value: $value")
                    if (value != "") {
                        val remoteConfig: RemoteConfigData =
                            GsonBuilder().create().fromJson(value, RemoteConfigData::class.java)
                        LogUtils.Print(TAG, "remoteConfig -> " + remoteConfig.androidAppOpenAds)
                        preferences.setRemoteConfig(remoteConfig)
                    }
                    LogUtils.Print(TAG, "Config params updated: $updated")
                    LogUtils.Print(TAG, "Fetch and activate succeeded")
                } else {
                    LogUtils.Print(TAG, "Fetch failed")
                }
                OneSignal.setAppId(preferences.getRemoteConfig()?.androidOneSignalID.toString())
                if (preferences.getBoolean(Constants.isFirstTime)) {
                    preferences.putBoolean(Constants.isFirstTime, false)
                }
                LogUtils.Print(TAG, "isOpenAdLoader => " + isOpenAdLoader)
                needToWait = false
                if (!isOpenAdLoader && preferences.getRemoteConfig()?.isShowAndroidAds!! && !preferences.getBoolean(
                        Constants.is_first_time
                    )
                ) {
                    if (appOpenManager != null) {
                        appOpenManager!!.showAdIfAvailable()
                        needToWait = true
                    }
                }
                Handler().postDelayed(
                    { getApplicationData() },
                    if (needToWait) 2000 else 0.toLong()
                )
            }
        } else {
            showSnackBarWithAction()
        }
    }

    override fun onResume() {
        super.onResume()
        if (needToWait || preferences.isProVersion()) {
            splash()
        }
    }

    /**
     * GET APPLICATION DATA IF NEEDED
     */
    private fun getApplicationData() {
        LogUtils.Print("Firebase", FirebaseDatabase.getInstance().reference.toString())
        if (!preferences.getString(Constants.updateFirebaseJson)
                .equals(preferences.getRemoteConfig()?.updateFirebaseJson)
        ) {
            FirebaseDatabase.getInstance().reference
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val usageCount = snapshot.child("Users").child(android_id).child("all_service").child("usageCount").getValue(Long::class.java)
                        val generateImageService = snapshot.child("Users").child(android_id).child("generate_image_service").child("throttle").getValue(Long::class.java)
                        val firstTimeUsedTimestamp = snapshot.child("Users").child(android_id).child("generate_image_service").child("firstTimeUsedTimestamp").getValue(Long::class.java)
//                        val responseClass = snapshot.getValue(all_service::class.java)
                        preferences.putInt(Constants.allService, usageCount!!.toInt())
                        preferences.putInt(Constants.generateImageService, generateImageService!!.toInt())
                        preferences.putLong(Constants.firstTimeUsedTimestamp, firstTimeUsedTimestamp!!)

                            val allService = all_service()
                            allService.usageCount = usageCount.toInt()
                            val generateImageService1 = GenerateImageService()
                            generateImageService1.throttle = generateImageService.toInt()
                            generateImageService1.firstTimeUsedTimestamp = firstTimeUsedTimestamp.toDouble()
                            val map = HashMap<String, Any>()
                            map["all_service"] = allService
                            map["generate_image_service"] = generateImageService1
                            database
                                .child("Users")
                                .child(Secure.getString(
                                    this@SplashActivity.getContentResolver(),
                                    Secure.ANDROID_ID))
                                .setValue(map)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        LogUtils.Print("TAG", "isSuccessful - " + task.toString())
                                    } else {
                                        LogUtils.Print("TAG", "Failure - " + task.exception)
                                    }
                                }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        Utils.makeToast(this@SplashActivity, getString(R.string.server_error))
                    }
                })

            FirebaseDatabase.getInstance().reference
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val responseClass = snapshot.getValue(Response::class.java)!!
                        preferences.setData(responseClass.data!!)
                        LogUtils.Print(TAG, "Value is: $responseClass")
                    }
                    override fun onCancelled(error: DatabaseError) {
                        Utils.makeToast(
                            this@SplashActivity, getString(R.string.server_error)
                        )
                    }
                })
        } else {
            LogUtils.Print(TAG, "Already updated.")
        }
    }

    override fun onBackPressed() {
        if (!isBack) {
            isBack = true
            Toast.makeText(
                this@SplashActivity,
                resources.getString(R.string.text_press_back_to_exit),
                Toast.LENGTH_SHORT
            ).show()
            Handler().postDelayed({ isBack = false }, 2000)
        } else {
            finish()
        }
    }

    private fun showSnackBarWithAction() {
        val snackBar =
            Snackbar.make(binding.root, getString(R.string.internet_error), Snackbar.LENGTH_LONG)
                .setAction(
                    getString(R.string.retry)
                ) { view: View? -> getRemoteConfigSettings() }
        snackBar.setActionTextColor(Color.WHITE)
        val sbView = snackBar.view
        sbView.setBackgroundColor(ContextCompat.getColor(this@SplashActivity, R.color.colorPrimary))
        snackBar.duration = BaseTransientBottomBar.LENGTH_INDEFINITE
        snackBar.show()
    }

    private fun splash() {
        Handler(Looper.getMainLooper()).postDelayed({
            if (preferences.getBoolean(Constants.first_open)) {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, IntroActivity::class.java)
                startActivity(intent)
            }
            finish()
        }, Constants.SPLASH_WAIT)
    }

    private fun checkState(status: Int, message: String) {
        when (status) {
            Constants.PURCHASED -> {
                preferences.putInt(
                    Constants.isProVersion, Constants.pro_version
                )
                preferences.putInt(
                    Constants.PRO_IMAGE_COUNTER,
                    preferences.getRemoteConfig()?.adsPresentCount
                )
            }

            Constants.NOT_PURCHASED -> {
                preferences.putInt(
                    Constants.isProVersion, Constants.not_pro_version
                )
                preferences.putInt(
                    Constants.PRO_IMAGE_COUNTER,
                    preferences.getRemoteConfig()?.freeCount
                )
            }

            Constants.ERR_MESSAGE -> {
                Utils.makeToast(this, message)
            }
        }
    }
}