package com.tc.utils.utils.helpers

import android.app.*
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi

import java.util.ArrayList

class HelperNotificationChannel(activity: Activity) {

    private var notificationManager: NotificationManager? = null

    init {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager = activity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                createNotificationGroup()
                setChannel(
                        GROUP_IN_APP,
                        CHANNEL_IN_APP,
                        "In-App",
                        "In-App notification notify you about your activities in app."
                )
                setChannel(
                        GROUP_VIDEOS,
                        CHANNEL_VIDEOS,
                        "Videos",
                        "We will notify you about Latest Videos added."
                )
            }
        } catch (e: Exception) {
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun createNotificationGroup() {
        val groups = ArrayList<NotificationChannelGroup>()
        groups.add(NotificationChannelGroup(GROUP_IN_APP, "In-App"))
        groups.add(NotificationChannelGroup(GROUP_VIDEOS, "Videos"))
        notificationManager!!.createNotificationChannelGroups(groups)
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun setChannel(group: String, tag: String, name: String, description: String) {
        val mChannel = NotificationChannel(tag, name, NotificationManager.IMPORTANCE_HIGH)
        mChannel.description = description
        mChannel.enableLights(true)
        mChannel.group = group
        mChannel.setShowBadge(true)
        mChannel.lightColor = Color.MAGENTA
        mChannel.enableVibration(false)
        mChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        mChannel.vibrationPattern = longArrayOf(0)
        notificationManager!!.createNotificationChannel(mChannel)
    }

    companion object {
        const val GROUP_IN_APP = "com.studiuz.studiuz.group.one"
        const val CHANNEL_IN_APP = "com.studiuz.studiuz.channel.one"

        const val GROUP_VIDEOS = "com.studiuz.studiuz.group.two"
        const val CHANNEL_VIDEOS = "com.studiuz.studiuz.two.channel.one"
    }
}
