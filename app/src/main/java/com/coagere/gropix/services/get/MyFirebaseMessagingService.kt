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
import androidx.core.app.TaskStackBuilder
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.coagere.gropix.R
import com.coagere.gropix.prefs.TempStorage
import com.coagere.gropix.ui.activities.MainActivity
import com.coagere.gropix.utils.ParseJson
import com.tc.utils.utils.helpers.CheckOs.checkBuildForLolipop
import com.tc.utils.utils.helpers.HelperNotificationChannel
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
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        try {
            if (!remoteMessage.data.isNullOrEmpty()) {
                val jsonObject = JSONObject(remoteMessage.data as Map<String, String>)
                val random = Random()
                random.nextInt()
                val title = jsonObject.getString("title")
                val body = jsonObject.getString("body")
                var url: String? = null
                if (ParseJson.instance.dataCheck(jsonObject, "link")) {
                    url = jsonObject.getString("link")
                }
                var image: String? = null
                if (ParseJson.instance.dataCheck(jsonObject, "image")) {
                    image = jsonObject.getString("image")
                }
                val id = random.nextInt(1000 - 10 + 1) + 10.toLong()
                setNotificationParsing(
                    title,
                    body,
                    image,
                    id
                )
            } else {
                remoteMessage.notification?.let {
                    val random = Random()
                    random.nextInt()
                    setNotificationParsing(
                        remoteMessage.notification!!.title,
                        remoteMessage.notification!!.body,
                        if (remoteMessage.notification!!
                                .icon != null
                        ) remoteMessage.notification!!.icon.toString()
                        else null,
                        random.nextInt(1000 - 10 + 1) + 10.toLong()
                    )
                }
            }
        } catch (e: Exception) {
            L.logE(e.message)
        }
    }

    private fun setNotificationParsing(
        title: String?,
        body: String?,
        image: String?,
        id: Long
    ) {
        builder = NotificationCompat.Builder(
            this,
            HelperNotificationChannel.CHANNEL_IN_APP
        )
            .setContentTitle(title)
            .setContentText(body)
            .setGroup(HelperNotificationChannel.GROUP_IN_APP)
        if (!image.isNullOrEmpty()) {
            if (Patterns.WEB_URL.matcher(image).matches()) {
                AsyncTask.execute {
                    Glide.with(this)
                        .asBitmap()
                        .load(image)
                        .centerCrop()
                        .into(object : CustomTarget<Bitmap>() {
                            override fun onLoadCleared(placeholder: Drawable?) {
                            }

                            override fun onLoadFailed(errorDrawable: Drawable?) {
                                super.onLoadFailed(errorDrawable)
                                createNotification(title, body, id)
                            }

                            override fun onResourceReady(
                                bitmap: Bitmap,
                                transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                            ) {
                                showBigNotification(bitmap, title, body, id)
                            }
                        })
                }
            }
        } else {
            createNotification(title, body, id)
        }
    }

    private fun createNotification(
        title: String?,
        message: String?,
        tag: Long
    ) {
        builder?.setStyle(
            NotificationCompat.InboxStyle()
                .setBigContentTitle(title)
                .setSummaryText(message)
        )
        finalizeNotification(tag, title)

    }

    private fun finalizeNotification(id: Long, title: String?) {
        var pendingIntent: PendingIntent? = null
        val taskStackBuilder = TaskStackBuilder.create(this)
        val intentPending = Intent(this, MainActivity::class.java)
        taskStackBuilder.addParentStack(MainActivity::class.java)
        taskStackBuilder.addNextIntent(intentPending)
        pendingIntent =
            taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        builder?.setContentIntent(pendingIntent)
        builder?.setAutoCancel(true)
            ?.setLargeIcon(
                BitmapFactory.decodeResource(
                    resources,
                    R.mipmap.ic_launcher
                )
            )
            ?.setOnlyAlertOnce(true)
            ?.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            ?.setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
            ?.setDefaults(NotificationCompat.DEFAULT_VIBRATE or NotificationCompat.DEFAULT_SOUND)?.priority =
            NotificationCompat.PRIORITY_DEFAULT
        if (checkBuildForLolipop()) {
            builder?.color = ContextCompat.getColor(this, R.color.colorPrimary)
//            builder?.setSmallIcon(R.drawable.app_logo);
        } else {
            builder!!.setSmallIcon(R.mipmap.ic_launcher)
        }
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager.getNotificationChannel(
                    HelperNotificationChannel.CHANNEL_IN_APP
                ).importance != NotificationManager.IMPORTANCE_NONE
            ) {
                builder?.setChannelId(HelperNotificationChannel.CHANNEL_IN_APP)
                notificationManager.notify(id.toInt(), builder!!.build())
            }
        } else {
            notificationManager.notify(id.toInt(), builder!!.build())
        }

    }

    private fun showBigNotification(
        bitmap: Bitmap,
        title: String?,
        message: String?,
        id: Long
    ) {
        val bigPictureStyle = NotificationCompat.BigPictureStyle()
        if (title.isNullOrEmpty()) {
            bigPictureStyle.setBigContentTitle(getString(R.string.app_name))
        } else {
            bigPictureStyle.setBigContentTitle(title)
        }
        bigPictureStyle.setSummaryText(message)
        bigPictureStyle.bigPicture(bitmap)
        builder?.setAutoCancel(true)
            ?.setStyle(bigPictureStyle)
        finalizeNotification(id, title)
    }

}