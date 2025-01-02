package com.hashmob.aichat.base

import android.content.Context
import com.appsflyer.AppsFlyerLib
import com.appsflyer.attribution.AppsFlyerRequestListener
import com.facebook.FacebookSdk
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.initialization.InitializationStatus
import com.google.firebase.FirebaseApp
import com.hashmob.aichat.BuildConfig
import com.hashmob.aichat.di.component.DaggerAppComponent
import com.hashmob.aichat.util.AppOpenManager
import com.hashmob.aichat.util.LogUtils
import com.hashmob.aichat.util.Preferences
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class BaseApplication : DaggerApplication(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    override fun applicationInjector(): AndroidInjector<out DaggerApplication?> {
        return DaggerAppComponent.builder().application(this).build()
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        preferences = Preferences(this)
        MobileAds.initialize(this) {}
        MobileAds.initialize(this) { initializationStatus: InitializationStatus? -> }
        appOpenManager = AppOpenManager(this)

//        registerActivityForBugSense(getApplicationContext());
        FirebaseApp.initializeApp(applicationContext)
        FacebookSdk.sdkInitialize(applicationContext)
        //        OneSignal.setRemoteNotificationReceivedHandler(new ExampleNotificationReceivedHandler());
//        OneSignal.disablePush(preferences.getBoolean(Constants.notification));

        //fb ads
//        AudienceNetworkAds.initialize(this);
        AppsFlyerLib.getInstance().init(BuildConfig.appsflyerKey, null, this);
        AppsFlyerLib.getInstance().start(this, BuildConfig.appsflyerKey, object :
            AppsFlyerRequestListener {
            override fun onSuccess() {
                LogUtils.Print("AppsFlyerLib", "Launch sent successfully")
            }

            override fun onError(errorCode: Int, errorDesc: String) {
                LogUtils.Print(
                    "AppsFlyerLib", "Launch failed to be sent:\n" +
                            "Error code: " + errorCode + "\n"
                            + "Error description: " + errorDesc
                )
            }
        })
    }

    fun initDagger() {
        DaggerAppComponent.builder().build().inject(this)
    }

    companion object {
        lateinit var context: Context
        var isOpenAdLoader = false
        var appOpenManager: AppOpenManager? = null
        lateinit var preferences: Preferences

        fun createRequestBody(s: String): RequestBody {
            return s
                .toRequestBody("multipart/form-data".toMediaTypeOrNull())
        }

        fun createRequestBody(file: File): RequestBody {
            return file
                .asRequestBody("multipart/form-data".toMediaTypeOrNull())
        }
    }
}