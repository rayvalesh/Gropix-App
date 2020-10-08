package com.tc.utils.utils.helpers

import android.content.Context
import android.os.Build
import android.provider.Settings

class DeviceInfo {
    val androidVersion: String
        get() {
            val release = Build.VERSION.RELEASE
            val sdkVersion = Build.VERSION.SDK_INT
            return "Android SDK: $sdkVersion ($release)"
        }
    val deviceName: String
        get() {
            val manufacture = Build.MANUFACTURER
            val model = Build.MODEL
            return if (model.startsWith(manufacture)) {
                model
            } else {
                "$manufacture $model"
            }
        }

    fun getDeviceId(context: Context): String {
        return Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )
    }
}