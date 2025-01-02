package com.hashmob.aichat.util

import android.content.Context
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase.PurchaseState
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.hashmob.aichat.base.BaseApplication.Companion.preferences
import com.hashmob.aichat.constants.Constants
import com.hashmob.aichat.util.LogUtils.Print

object PremiumUtils {
    private var mBillingClient: BillingClient? = null
    private const val TAG = "PremiumUtils"
    fun checkSubscriptionStatus(context: Context, hasPurchased: ((Int, String) -> Unit)) {
        mBillingClient = BillingClient.newBuilder(context).setListener(purchasesUpdatedListener)
            .enablePendingPurchases().build()

        mBillingClient!!.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    Print("TAG", "onBillingSetupFinished")

                    val product = QueryPurchasesParams.newBuilder()
                        .setProductType(BillingClient.ProductType.SUBS).build()

                    mBillingClient!!.queryPurchasesAsync(product) { billingResult, purchases ->
                        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                            if (purchases.isNotEmpty()) {
                                Print(TAG, "Purchases :---$purchases")
                                for (purchase in purchases) {
                                    for (p in purchase.products) {
                                        Print("TAG", "productId -> $p")
                                        if (p == Constants.SUBSCRIPTION_TEST_PER_WEEK || p == Constants.SUBSCRIPTION_TEST_PER_MONTH) {
                                            Print(
                                                "TAG",
                                                "purchaseState -> " + purchase.purchaseState
                                            )
                                            if (purchase.purchaseState == PurchaseState.PURCHASED) {
                                                Print("TAG", "User has an active subscription")
                                                preferences.putInt(
                                                    Constants.isProVersion, Constants.pro_version
                                                )
                                                preferences.putInt(
                                                    Constants.PRO_IMAGE_COUNTER,
                                                    preferences.getRemoteConfig()?.adsPresentCount
                                                )
                                                hasPurchased.invoke(
                                                    Constants.PURCHASED,
                                                    billingResult.debugMessage
                                                )
                                            } else {
                                                Print(
                                                    "TAG",
                                                    "User does not have an active subscription"
                                                )
                                                preferences.putInt(
                                                    Constants.isProVersion,
                                                    Constants.not_pro_version
                                                )
                                                preferences.putInt(
                                                    Constants.PRO_IMAGE_COUNTER,
                                                    preferences.getRemoteConfig()?.freeCount
                                                )
                                                hasPurchased.invoke(
                                                    Constants.NOT_PURCHASED,
                                                    billingResult.debugMessage
                                                )
                                            }
                                            break
                                        }
                                    }
                                }
                            } else {
                                hasPurchased.invoke(
                                    Constants.NOT_PURCHASED,
                                    billingResult.debugMessage
                                )
                                Print(
                                    TAG,
                                    "Code :- ${billingResult.responseCode} Message :- ${billingResult.debugMessage}"
                                )
                            }
                        } else {
                            hasPurchased.invoke(Constants.ERR_MESSAGE, billingResult.debugMessage)
                            Print(
                                TAG,
                                "Code :- ${billingResult.responseCode} Message :- ${billingResult.debugMessage}"
                            )
                        }
                    }
                } else {
                    hasPurchased.invoke(Constants.ERR_MESSAGE, billingResult.debugMessage)
                    Print(
                        TAG,
                        "Code :- ${billingResult.responseCode} Message :- ${billingResult.debugMessage}"
                    )
                }
            }

            override fun onBillingServiceDisconnected() {
                Print("TAG", "onBillingServiceDisconnected -> ")
            }
        })
    }

    fun restorePlan(hasRestore: (Boolean) -> Unit) {
        mBillingClient!!.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    val params = QueryPurchasesParams.newBuilder()
                        .setProductType(BillingClient.ProductType.SUBS).build()
                    mBillingClient!!.queryPurchasesAsync(params) { result, list ->
                        Print(TAG, "billingResult $result")
                        Print(TAG, "onConsumeFinished $list")
                        if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                            if (list.isEmpty()) {
                                hasRestore.invoke(false)
                                Print("TAG", "User does not have an restore subscription")
                            } else {
                                for (purchase in list) {
                                    for (p in purchase.products) {
                                        Print("TAG", "productId -> $p")
                                        if (p == Constants.SUBSCRIPTION_TEST_PER_WEEK || p == Constants.SUBSCRIPTION_TEST_PER_MONTH) {
                                            Print(
                                                "TAG",
                                                "purchaseState -> " + purchase.purchaseState
                                            )
                                            if (purchase.purchaseState == PurchaseState.PURCHASED) {
                                                Print("TAG", "User has an active subscription")
                                                hasRestore.invoke(true)
                                            } else {
                                                Print(
                                                    "TAG",
                                                    "User does not have an restore subscription"
                                                )
                                                hasRestore.invoke(false)
                                            }
                                            break
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            override fun onBillingServiceDisconnected() {
                Print("TAG", "onBillingServiceDisconnected -> ")
            }
        })
    }


    fun handleBilling(position: Int) {
        Print("TAG", "handleBilling")
        mBillingClient!!.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    Print("TAG", "onBillingSetupFinished")

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
                    ) { billingResult12: BillingResult, list: List<ProductDetails> ->
                        Print(
                            "TAG",
                            "billingResult1.getResponseCode() " + billingResult12.responseCode
                        )
                        Print("TAG", "list $list")
                        if (billingResult12.responseCode == BillingClient.BillingResponseCode.OK) {
                            Print("TAG", "BillingResponseCode.OK")
                            for (i in list.indices) {
                                if (list[i].productId.equals(if (position != 0) { Constants.SUBSCRIPTION_TEST_PER_MONTH } else { Constants.SUBSCRIPTION_TEST_PER_WEEK })) {
                                    break
                                }
                            }
                        }
                    }
                }
            }

            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                Print("TAG", "onBillingServiceDisconnected -> ")
            }
        })
    }


    private val purchasesUpdatedListener =
        PurchasesUpdatedListener { billingResult, purchases ->
            // To be implemented in a later section.
            Print(
                "TAG",
                "onPurchasesUpdated -> " + if (purchases != null && purchases.size > 0) purchases[0].products else ""
            )
            Print("TAG", "getResponseCode -> " + billingResult.responseCode)
        }
}