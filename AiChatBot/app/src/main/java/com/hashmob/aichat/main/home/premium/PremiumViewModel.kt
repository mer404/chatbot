package com.hashmob.aichat.main.home.premium

import android.content.Intent
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.hashmob.aichat.R
import com.hashmob.aichat.constants.Constants
import com.hashmob.aichat.constants.Constants.isProVersion
import com.hashmob.aichat.data.repository.MainRepository
import com.hashmob.aichat.databinding.ActivityPremiumBinding
import com.hashmob.aichat.main.home.ui.HomeActivity
import com.hashmob.aichat.util.LogUtils
import com.hashmob.aichat.util.PremiumUtils
import com.hashmob.aichat.util.Utils
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject


class PremiumViewModel @Inject constructor(var authRepository: MainRepository) : ViewModel() {
    lateinit var activity: PremiumActivity
    lateinit var binding: ActivityPremiumBinding
    private var adapter: PremiumAdapter? = null
    private lateinit var list: ArrayList<PremiumModel>
    var mBillingClient: BillingClient? = null
    private val TAG = "BuyProViewModel"

    fun initialize(
        activity: PremiumActivity, binding: ActivityPremiumBinding
    ) {
        this.activity = activity
        this.binding = binding
    }

    fun initView() {
        val spannableString =
            SpannableString(activity.resources.getString(R.string.get_full_access))
        val foregroundSpan = ForegroundColorSpan(activity.resources.getColor(R.color.colorTextSend))
        spannableString.setSpan(
            foregroundSpan, 4, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.tvGetFullAccess.text = spannableString
    }

    fun onClick(view: View) {
        when (view) {
            binding.tvPrivacyPolicy -> {
                Utils.termsPrivacy(activity, 0)
            }

            binding.tvTermsOfUse -> {
                Utils.termsPrivacy(activity, 1)
            }

            binding.ivClose -> {
                activity.onBackPressedDispatcher.onBackPressed()
            }
            binding.btnContinue -> {
                purchasePlan(PremiumAdapter.selectedPosition)
//                activity.preferences.putInt(
//                    isProVersion, Constants.pro_version
//                )
//                activity.preferences.putInt(
//                    Constants.PRO_IMAGE_COUNTER,
//                    activity.preferences.getRemoteConfig()?.adsPresentCount
//                )
            }
            binding.tvRestore -> {
                PremiumUtils.restorePlan(this::restorePurchase)
            }
        }
    }

    fun setData() {
        list = ArrayList()
        list.clear()
        list.add(
            PremiumModel(
                activity.getString(R.string.weekly_access),
                activity.getString(R.string._299),
                false,
                activity.getString(R.string.popular)
            )
        )
        list.add(
            PremiumModel(
                activity.getString(R.string.monthly_access),
                activity.getString(R.string._999),
                false,
                activity.getString(R.string.best_offer)
            )
        )
        adapter = PremiumAdapter(activity, list)
        binding.rcvPremium.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.rcvPremium.adapter = adapter
    }

    private fun purchasePlan(position: Int) {
        mBillingClient = BillingClient.newBuilder(activity).setListener(purchasesUpdatedListener)
            .enablePendingPurchases().build()
        mBillingClient!!.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    LogUtils.Print("TAG", "onBillingSetupFinished")

                    val product = QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(
                            if (position != 0) {
                                Constants.SUBSCRIPTION_TEST_PER_MONTH
                            } else {
                                Constants.SUBSCRIPTION_TEST_PER_WEEK
                            }
                        )
                        .setProductType(BillingClient.ProductType.SUBS).build()
                    val params = QueryProductDetailsParams.newBuilder()
                    params.setProductList(listOf(product))
                    mBillingClient!!.queryProductDetailsAsync(
                        params.build()
                    ) { billingResult12: BillingResult, list: List<ProductDetails?> ->
                        LogUtils.Print(
                            "TAG",
                            "billingResult1.getResponseCode() " + billingResult12.responseCode
                        )
                        LogUtils.Print("TAG", "list_params $params")
                        LogUtils.Print("TAG", "list size ${list.size}")
                        if (list.isNotEmpty()) {
                            LogUtils.Print("TAG", "list obj ${list[0]}")
                            val productDetailsParamsList = listOf(
                                BillingFlowParams.ProductDetailsParams.newBuilder()
                                    .setProductDetails(list[0]!!)
                                    .setOfferToken(list[0]!!.subscriptionOfferDetails!![0].offerToken)
                                    .build()
                            )

                            val billingFlowParams = BillingFlowParams.newBuilder()
                                .setProductDetailsParamsList(productDetailsParamsList)
                                .build()

                            val billingResponseCode =
                                mBillingClient!!.launchBillingFlow(
                                    activity,
                                    billingFlowParams
                                )
                            if (billingResponseCode.responseCode == BillingClient.BillingResponseCode.OK) {
                                LogUtils.Print(
                                    "TAG",
                                    "querySkuDetailsAsync -> $billingResponseCode"
                                )
                            }
                        }
                    }
                }
            }

            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                LogUtils.Print("TAG", "onBillingServiceDisconnected -> ")
            }
        })
    }

    private fun handlePurchase(purchase: Purchase, billingClient: BillingClient) {
        LogUtils.Print("TAG", "purchase => $purchase")
        val acknowledgePurchaseParams = AcknowledgePurchaseParams
            .newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()
        billingClient.acknowledgePurchase(
            acknowledgePurchaseParams
        ) { billingResult ->
            when (billingResult.responseCode) {
                BillingClient.BillingResponseCode.OK -> {
                    // Handle the success of the consume operation.
                    LogUtils.Print("TAG", "=================")
                    LogUtils.Print("TAG", "==== SUCCESS ====")
                    LogUtils.Print("TAG", "=================")
                    activity.preferences.putBoolean(isProVersion, true)
                    activity.preferences.putInt(
                        isProVersion, Constants.pro_version
                    )
                    activity.preferences.putInt(
                        Constants.PRO_IMAGE_COUNTER,
                        activity.preferences.getRemoteConfig()?.adsPresentCount
                    )
                    Utils.setFirebaseData(activity,activity.preferences.getInt(Constants.allService),true,false)
                    activity.runOnUiThread {
                        Utils.makeToast(
                            activity,
                            activity.resources.getString(R.string.success_you_purchased_the_pro_version)
                        )
                        activity.startActivity(
                            Intent(activity, HomeActivity::class.java).setFlags(
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            )
                        )
                        activity.finish()
                    }
                }

                BillingClient.BillingResponseCode.DEVELOPER_ERROR -> {
                    LogUtils.Print(
                        "TAG",
                        "==== DEVELOPER_ERROR ==== +${billingResult.debugMessage}"
                    )
                }

                BillingClient.BillingResponseCode.SERVICE_DISCONNECTED -> {
                    LogUtils.Print(
                        "TAG",
                        "==== SERVICE_DISCONNECTED ==== +${billingResult.debugMessage}"
                    )
                }

                BillingClient.BillingResponseCode.NETWORK_ERROR -> {
                    LogUtils.Print("TAG", "==== NETWORK_ERROR ==== +${billingResult.debugMessage}")
                }

                BillingClient.BillingResponseCode.ITEM_UNAVAILABLE -> {
                    LogUtils.Print(
                        "TAG",
                        "==== ITEM_UNAVAILABLE ==== +${billingResult.debugMessage}"
                    )
                }

                BillingClient.BillingResponseCode.FEATURE_NOT_SUPPORTED -> {
                    LogUtils.Print(
                        "TAG",
                        "==== FEATURE_NOT_SUPPORTED ==== +${billingResult.debugMessage}"
                    )
                }

                BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED -> {
                    LogUtils.Print(
                        "TAG",
                        "==== ITEM_ALREADY_OWNED ==== +${billingResult.debugMessage}"
                    )
                }

                BillingClient.BillingResponseCode.ERROR -> {
                    LogUtils.Print("TAG", "==== ERROR ==== +${billingResult.debugMessage}")
                }

                BillingClient.BillingResponseCode.USER_CANCELED -> {
                    LogUtils.Print("TAG", "==== USER_CANCELED ==== +${billingResult.debugMessage}")
                }

                BillingClient.BillingResponseCode.BILLING_UNAVAILABLE -> {
                    LogUtils.Print(
                        "TAG",
                        "==== BILLING_UNAVAILABLE ==== +${billingResult.debugMessage}"
                    )
                }

                BillingClient.BillingResponseCode.SERVICE_UNAVAILABLE -> {
                    LogUtils.Print(
                        "TAG",
                        "==== SERVICE_UNAVAILABLE ==== +${billingResult.debugMessage}"
                    )
                }

                BillingClient.BillingResponseCode.ITEM_NOT_OWNED -> {
                    LogUtils.Print("TAG", "==== ITEM_NOT_OWNED ==== +${billingResult.debugMessage}")
                }
            }
        }
    }

    private fun restorePurchase(hasRestore: Boolean) {
        if (hasRestore) {
            activity.preferences.putInt(
                isProVersion, Constants.not_pro_version
            )
            activity.preferences.putInt(
                Constants.PRO_IMAGE_COUNTER,
                activity.preferences.getRemoteConfig()?.freeCount
            )
            activity.runOnUiThread {
                Utils.makeToast(
                    activity,
                    activity.resources.getString(R.string.restore_purchase_successfully)
                )
                activity.startActivity(
                    Intent(
                        activity,
                        HomeActivity::class.java
                    ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                )
                activity.finish()
            }
        } else {
            activity.runOnUiThread {
                Utils.makeToast(
                    activity,
                    activity.resources.getString(R.string.you_does_not_have_restore_purchase)
                )
            }
        }
    }

    private val purchasesUpdatedListener =
        PurchasesUpdatedListener { billingResult, purchases ->
            // To be implemented in a later section.
            LogUtils.Print(
                "TAG",
                "onPurchasesUpdated -> " + if (purchases != null && purchases.size > 0) purchases[0].products else ""
            )
            LogUtils.Print("TAG", "getResponseCode -> " + billingResult.responseCode)
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                // do something you want
                for (purchase in purchases) {
                    for (p in purchase.products) {
                        LogUtils.Print("TAG", "productId -> $p")
                        if (p == Constants.SUBSCRIPTION_TEST_PER_MONTH || p == Constants.SUBSCRIPTION_TEST_PER_WEEK) {
                            handlePurchase(purchase, mBillingClient!!)
                            break
                        }
                    }
                }
            } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
                LogUtils.Print("TAG", "onPurchasesUpdated -> " + billingResult.debugMessage)
            } else {
                LogUtils.Print("TAG", "onPurchasesUpdated -> " + billingResult.debugMessage)
            }
        }
}