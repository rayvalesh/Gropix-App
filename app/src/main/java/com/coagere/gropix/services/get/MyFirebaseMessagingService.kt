package com.coagere.gropix.services.get

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.os.Build
import android.util.Patterns
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.coagere.gropix.R
import com.coagere.gropix.jetpack.repos.UserRepo
import com.coagere.gropix.prefs.TempStorage
import com.coagere.gropix.ui.activities.ExploreOrderActivity
import com.coagere.gropix.ui.activities.MainActivity
import com.coagere.gropix.utils.MyApplication
import com.coagere.gropix.utils.ParseJson
import com.tc.utils.utils.helpers.CheckOs.checkBuildForLolipop
import com.tc.utils.utils.helpers.HelperNotificationChannel
import com.tc.utils.utils.helpers.Utils
import com.tc.utils.variables.interfaces.Constants
import com.tc.utils.variables.interfaces.IntentInterface
import org.json.JSONException
import org.json.JSONObject
import tk.jamun.ui.snacks.L
import java.util.*

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private var builder: NotificationCompat.Builder? = null

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        FirebaseMessaging.getInstance().subscribeToTopic("message")
        TempStorage.instance.saveFcmToken(token)
        TempStorage.instance.saveAppStatus = false
        if (MyApplication.isLoggedIn) {
            UserRepo.instance.postFcm()
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        try {
            if (!remoteMessage.data.isNullOrEmpty()) {
                val jsonObject = JSONObject(remoteMessage.data as Map<String, String>)
                Utils.log(jsonObject.toString())
                val random = Random()
                random.nextInt()
                val title = jsonObject.getString("title")
                val body = jsonObject.getString("body")
                val taskStackBuilder = TaskStackBuilder.create(this)
                taskStackBuilder.addParentStack(MainActivity::class.java)
                taskStackBuilder.addNextIntent(Intent(this, MainActivity::class.java))
                var summaryText: String? = null
                if (dataCheck(jsonObject, "type")) {
                     taskStackBuilder.addNextIntent(
                        Intent(this, ExploreOrderActivity::class.java)
                            .putExtra(
                                IntentInterface.INTENT_FOR_ID,
                                jsonObject.getString("orderId")
                            ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    )
                    summaryText = when (jsonObject.getInt("type")) {
                        Constants.ORDER_CART -> {
                            getString(R.string.string_label_status_cart)
                        }
                        Constants.ORDER_PENDING -> {
                            getString(R.string.string_label_status_placed)
                        }
                        Constants.ORDER_CONFIRMED -> {
                            getString(R.string.string_label_status_confirmed)
                        }
                        else -> {
                            getString(R.string.string_label_status_out_delivery)
                        }
                    }
                }
                val id = random.nextInt(1000 - 10 + 1) + 10.toLong()
                setNotificationParsing(
                    title,
                    body,
                    summaryText,
                    id,
                    taskStackBuilder
                )
            } else {
                remoteMessage.notification?.let {
                    val random = Random()
                    random.nextInt()
                    setNotificationParsing(
                        remoteMessage.notification!!.title,
                        remoteMessage.notification!!.body,
                        null,
                        random.nextInt(1000 - 10 + 1) + 10.toLong()
                    )
                }
            }
        } catch (e: JSONException) {
            L.logE(e.message)
        }
    }

    fun dataCheck(jsonObject: JSONObject?, key: String): Boolean {
        return jsonObject != null && jsonObject.has(key) && !jsonObject.isNull(key)
    }

    private fun setNotificationParsing(
        title: String?,
        body: String?,
        summaryText: String?,
        id: Long,
        taskStackBuilder: TaskStackBuilder? = null
    ) {
        builder = NotificationCompat.Builder(
            this,
            HelperNotificationChannel.CHANNEL_IN_APP
        ).setContentTitle(title)
            .setContentText(body)
            .setGroup(HelperNotificationChannel.GROUP_IN_APP)
        taskStackBuilder?.let {
            builder?.setContentIntent(it.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT))
        }
        createNotification(title, body, summaryText, id)
    }

    private fun createNotification(
        title: String?,
        message: String?,
        summaryText: String?,
        tag: Long
    ) {
        builder?.setStyle(
            NotificationCompat.InboxStyle()
                .setBigContentTitle(title)
                .setSummaryText(summaryText)
                .addLine(message)
        )
        finalizeNotification(tag, title)

    }

    private fun finalizeNotification(id: Long, title: String?) {
           builder?.setAutoCancel(true)
            ?.setLargeIcon(
                BitmapFactory.decodeResource(
                    resources,
                    R.mipmap.ic_launcher
                )
            )?.setChannelId(HelperNotificationChannel.CHANNEL_IN_APP)
            ?.setOnlyAlertOnce(true)
            ?.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            ?.setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
            ?.setDefaults(NotificationCompat.DEFAULT_VIBRATE or NotificationCompat.DEFAULT_SOUND)?.priority =
            NotificationCompat.PRIORITY_DEFAULT
        if (checkBuildForLolipop()) {
            builder?.color = ContextCompat.getColor(this, R.color.colorPrimary)
            builder?.setSmallIcon(R.drawable.image_logo);
        } else {
            builder!!.setSmallIcon(R.mipmap.ic_launcher)
        }

        NotificationManagerCompat.from(this).notify(id.toInt(), builder!!.build())

    }

}