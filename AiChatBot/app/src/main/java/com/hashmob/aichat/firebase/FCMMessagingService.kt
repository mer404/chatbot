package com.hashmob.aichat.firebase

import android.content.Context
import android.content.Intent
import com.hashmob.aichat.base.BaseApplication
import com.hashmob.aichat.constants.Constants
import com.hashmob.aichat.util.LogUtils
import com.hashmob.aichat.util.Preferences
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FCMMessagingService : FirebaseMessagingService() {
    lateinit var preferences: Preferences
    override fun onNewToken(s: String) {
        super.onNewToken(s)
        preferences= Preferences(BaseApplication.context)
        LogUtils.Print(TAG, "fcm_registration_id --> $s")

        if (preferences != null) preferences.putString(Constants.fcm_registration_id, s)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        LogUtils.Print(TAG, "From: " + remoteMessage.from)
        LogUtils.Print(TAG, "getNotification: " + remoteMessage.notification)
        LogUtils.Print(TAG, "getData: " + remoteMessage.data)
        setPushNotification(remoteMessage)
    }

    private fun setPushNotification(remoteMessage: RemoteMessage) {
        val title = remoteMessage.data[Constants.title]
        val body = remoteMessage.data[Constants.body]
        val badge = remoteMessage.data[Constants.badge]

//        val resultIntent = Intent(applicationContext, MainActivity::class.java)
//        resultIntent.putExtra(Constants.from, Constants.from_push)

        if (remoteMessage.data.containsKey("payload")) {
            val payload = remoteMessage.data["payload"]
//            val dataConversation: DataNotification.Payload = GsonBuilder().create().fromJson(
//                payload,
//                DataNotification.Payload::class.java
//            )
//
//            val userChat: ChatListData?
//            if (dataConversation.data != null
//                &&!dataConversation.data.equals("")) {
//                userChat = GsonBuilder().create().fromJson(dataConversation.data.toString(), ChatListData::class.java)
//
//                if(dataConversation.type.contentEquals("NORMAL")){
//                    resultIntent.putExtra(Constants.push_type, Constants.from_chat_push)
//                    resultIntent.putExtra(Constants.DATA, userChat)
//                }
//            }
        }

//        showNotificationMessage(
//            applicationContext,
//            if (title != null && title != "") title else getString(R.string.app_name),
//            if (body != null && body != "") body else getString(R.string.app_name),
//            resultIntent, badge!!
//        )
    }

    /**
     * SHOWING NOTIFICATION WITH TEXT ONLY
     */
    private fun showNotificationMessage(
        context: Context, title: String, message: String,
        intent: Intent, badge: String
    ) {
        val notificationUtils = NotificationUtils(context)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        notificationUtils.showNotificationMessage(title, message, intent, badge)
    }

    companion object {
        private const val TAG = "FCMMessagingService"

    }
}