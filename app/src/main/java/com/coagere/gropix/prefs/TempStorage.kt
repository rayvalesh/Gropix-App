package com.coagere.gropix.prefs

import android.content.Context
import android.content.SharedPreferences
import com.coagere.gropix.utils.MyApplication

class TempStorage private constructor(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(SHARE_TEMP_NAME, SHARED_MODE)

    val fcmToken: String?
        get() = sharedPreferences.getString("shared_fcm_token", null)

    fun saveFcmToken(token: String) {
        UserStorage.instance.isFcmSent = false
        sharedPreferences.edit().putString("shared_fcm_token", token).apply()
    }

    var appId: Int
        get() = sharedPreferences.getInt("shared_app_id", 0)
        set(value) {
            sharedPreferences.edit().putInt("shared_app_id", value).apply()
        }

    var appRegistration: Boolean
        get() = sharedPreferences.getBoolean("shared_app_register", false)
        set(value) = sharedPreferences.edit().putBoolean("shared_app_register", value).apply()


    var saveAppStatus: Boolean
        get() = sharedPreferences.getBoolean("tc_app_status", true)
        set(value) = sharedPreferences.edit().putBoolean("tc_app_status", value).apply()


    companion object {

        private const val SHARE_TEMP_NAME = "studiuz_storage_temp"
        private const val SHARED_MODE = Context.MODE_PRIVATE

        @get:Synchronized
        val instance: TempStorage = TempStorage(MyApplication.appContext)
    }
}
