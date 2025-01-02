package com.hashmob.aichat.data.model.firebase.remoteconfig

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RemoteConfigData {
    @SerializedName("updateFirebaseJson")
    @Expose
    var updateFirebaseJson: String? = null

    @SerializedName("iosUpdateVersion")
    @Expose
    var iosUpdateVersion: String? = null

    @SerializedName("iosUpdateDescription")
    @Expose
    var iosUpdateDescription: String? = null

    @SerializedName("androidUpdateVersion")
    @Expose
    var androidUpdateVersion: String? = null

    @SerializedName("androidUpdateDescription")
    @Expose
    var androidUpdateDescription: String? = null

    @SerializedName("isShowiOSAds")
    @Expose
    var isShowiOSAds = false

    @SerializedName("isShowAndroidAds")
    @Expose
    var isShowAndroidAds = false

    @SerializedName("isProVersion")
    @Expose
    var isProVersion = 0

    @SerializedName("iosBannerAds")
    @Expose
    var iosBannerAds: String? = null

    @SerializedName("iosInterstitalAds")
    @Expose
    var iosInterstitalAds: String? = null

    @SerializedName("iosAppOpenAds")
    @Expose
    var iosAppOpenAds: String? = null

    @SerializedName("iosRewardedAds")
    @Expose
    var iosRewardedAds: String? = null

    @SerializedName("iosAppID")
    @Expose
    var iosAppID: String? = null

    @SerializedName("androidBannerAds")
    @Expose
    var androidBannerAds: String? = null

    @SerializedName("androidAdaptiveBannerAds")
    @Expose
    var androidAdaptiveBannerAds: String? = null

    @SerializedName("androidInterstitalAds")
    @Expose
    var androidInterstitalAds: String? = null

    @SerializedName("androidAppID")
    @Expose
    var androidAppID: String? = null

    @SerializedName("androidAppOpenAds")
    @Expose
    var androidAppOpenAds: String? = null

    @SerializedName("androidRewardedAds")
    @Expose
    var androidRewardedAds: String? = null

    @SerializedName("iosOneSignalID")
    @Expose
    var iosOneSignalID: String? = null

    @SerializedName("androidOneSignalID")
    @Expose
    var androidOneSignalID: String? = null

    @SerializedName("iOSPrivacyPolicy")
    @Expose
    var iOSPrivacyPolicy: String? = null

    @SerializedName("androidPrivacyPolicy")
    @Expose
    var androidPrivacyPolicy: String? = null

    @SerializedName("androidTemsService")
    @Expose
    var androidTemsService: String? = null

    @SerializedName("iosShareText")
    @Expose
    var iosShareText: String? = null

    @SerializedName("androidShareText")
    @Expose
    var androidShareText: String? = null

    @SerializedName("freeCount")
    @Expose
    var freeCount: Int? = null

    @SerializedName("adsPresentCount")
    @Expose
    var adsPresentCount: Int? = null

    @SerializedName("maxImageGenrate")
    @Expose
    var maxImageGenrate: Int? = null

    @SerializedName("androidTotalCoin")
    @Expose
    var androidTotalCoin: Int? = null

    @SerializedName("androidKeyHintCoin")
    @Expose
    var androidKeyHintCoin: Int? = null

    @SerializedName("androidKeyAnswerCoin")
    @Expose
    var androidKeyAnswerCoin: Int? = null

    @SerializedName("androidAdsCoin")
    @Expose
    var androidAdsCoin: Int? = null

    @SerializedName("contactUsEmail")
    @Expose
    var contactUsEmail: String? = null

    @SerializedName("config_test")
    @Expose
    var config_test: String? = null

    @SerializedName("Authorization")
    @Expose
    var Authorization: String? = null

}
