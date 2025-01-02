package com.hashmob.aichat.firebase

import android.app.*
import android.content.ComponentName
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.hashmob.aichat.R
import com.hashmob.aichat.constants.Constants
import java.text.SimpleDateFormat
import java.util.*

class NotificationUtils(private val mContext: Context) {
    val NOTIFICATION_CHANNEL_ID = "my_notification_channel"
    fun showNotificationMessage(title: String, message: String, intent: Intent, badge: String) {
        // Check for empty push message
        if (TextUtils.isEmpty(message)) return

        // notification icon
        val icon: Int = R.mipmap.ic_logo
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        val resultPendingIntent: PendingIntent = PendingIntent.getActivity(
            mContext,
            createID(),
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val strNotificationId = createNID()
        val mBuilder = NotificationCompat.Builder(
            mContext, strNotificationId
        )
        showSmallNotification(
            mBuilder,
            title,
            message,
            resultPendingIntent,
            strNotificationId,
            badge
        )
    }

    private fun showSmallNotification(
        mBuilder: NotificationCompat.Builder, title: String,
        message: String, resultPendingIntent: PendingIntent,
        strNotificationId: String, badge: String
    ) {
        val notificationManager: NotificationManager =
            mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                strNotificationId, title, NotificationManager.IMPORTANCE_HIGH
            )

            // Configure the notification channel.
            notificationChannel.setDescription(message)
            notificationChannel.enableLights(true)
            notificationChannel.setLightColor(Color.RED)
            notificationChannel.setVibrationPattern(longArrayOf(0, 1000, 500, 1000))
            notificationChannel.enableVibration(true)
            //            notificationChannel.setShowBadge(false);
            notificationChannel.setImportance(NotificationManager.IMPORTANCE_HIGH)
            if (notificationManager != null) notificationManager.createNotificationChannel(
                notificationChannel
            )
        }
        mBuilder.setSmallIcon(R.mipmap.ic_logo).setTicker(title).setWhen(0)
            .setAutoCancel(true)
            .setContentTitle(title)
            .setContentIntent(resultPendingIntent)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setSmallIcon(notificationSmallIcon)
            .setLargeIcon(BitmapFactory.decodeResource(mContext.resources, R.mipmap.ic_logo))
            .setContentText(message) //                .setNumber(4)
            //                .setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE)
            .setVibrate(longArrayOf(1000, 1000))
            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
            .setTicker(message)
            .setChannelId(strNotificationId)
            .build()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mBuilder.priority = NotificationManager.IMPORTANCE_MAX
        } else {
            mBuilder.priority = Notification.PRIORITY_HIGH
            //            playNotificationSound();
        }
        if (notificationManager != null) notificationManager.notify(createID(), mBuilder.build())

        //set badge count
//        if (ShortcutBadger.isBadgeCounterSupported(mContext))
//            ShortcutBadger.applyCount(mContext, (!TextUtils.isEmpty(badge) ? Integer.parseInt(badge) : 0));
        //for Xiaomi device
//        ShortcutBadger.applyNotification(mContext, mBuilder.build(), (!TextUtils.isEmpty(badge) ? Integer.parseInt(badge) : 0));
    }

    private fun createID(): Int {
        val now = Date()
        return SimpleDateFormat(Constants.DATE_PUSH_NOTIFICATION_FORMAT, Locale.US).format(now)
            .toInt()
    }

    private fun createNID(): String {
        val cDate = Date()
        return SimpleDateFormat(Constants.DATE_PUSH_NOTIFICATION_FORMAT, Locale.ENGLISH).format(
            cDate
        )
    }

    private val notificationSmallIcon: Int
        private get() {
            val useWhiteIcon: Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
            return if (useWhiteIcon) R.mipmap.ic_app_logo_round else R.mipmap.ic_app_logo_round
        }

    // Playing notification sound
    private fun playNotificationSound() {
        try {
            val alarmSound = Uri.parse(
                ContentResolver.SCHEME_ANDROID_RESOURCE
                        + "://" + mContext.packageName + "/raw/notification"
            )
            val r: Ringtone = RingtoneManager.getRingtone(mContext, alarmSound)
            r.play()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        /**
         * METHOD CHECKS IF THE APP IS IN BACKGROUND OR NOT
         */
        @RequiresApi(api = Build.VERSION_CODES.Q)
        fun isAppIsInBackground(context: Context): Boolean {
            var isInBackground = true
            val am: ActivityManager =
                context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
                val runningProcesses: List<ActivityManager.RunningAppProcessInfo> = am.getRunningAppProcesses()
                for (processInfo in runningProcesses) {
                    if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                        for (activeProcess in processInfo.pkgList) {
                            if (activeProcess == context.packageName) {
                                isInBackground = false
                            }
                        }
                    }
                }
            } else {
                val taskInfo: List<ActivityManager.RunningTaskInfo> = am.getRunningTasks(1)
                val componentInfo: ComponentName = taskInfo[0].topActivity!!
                if (componentInfo.getPackageName() == context.packageName) {
                    isInBackground = false
                }
            }
            return isInBackground
        }

        // Clears notification tray messages
        fun clearNotifications(context: Context) {
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancelAll()
        }
    }
}