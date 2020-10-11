package com.coagere.gropix.prefs

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.coagere.gropix.jetpack.entities.AddressModel
import com.coagere.gropix.jetpack.entities.ItemModel
import com.coagere.gropix.jetpack.entities.UserModel
import com.coagere.gropix.utils.MyApplication
import com.google.gson.reflect.TypeToken
import tk.jamun.volley.classes.VolleyGSON
import tk.jamun.volley.model.ModelHeader
import java.util.*

class UserStorage(context: Context) {


    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(SHARED_NAME, SHARED_MODE)

    var addressModel: AddressModel?
        get() = VolleyGSON.get().fromJson(
            VolleyGSON.get().getTypeToken(object : TypeToken<AddressModel?>() {}),
            sharedPreferences.getString(SHARED_ADDRESS, null)
        ) as AddressModel?
        set(value) = sharedPreferences.edit().putString(
            SHARED_ADDRESS,
            VolleyGSON.get()
                .toJson(
                    value,
                    VolleyGSON.get().getTypeToken(object : TypeToken<AddressModel?>() {})
                )
        ).apply()


    var isNotificationEnabled: Boolean
        get() = sharedPreferences.getBoolean(SHARED_SETTING_NOTIFICATION, true)
        set(value) = sharedPreferences.edit { putBoolean(SHARED_SETTING_NOTIFICATION, value) }

    val isLoggedIn: Boolean
        get() = sharedPreferences.getBoolean(SHARED_LOGIN_CHECK, false)

    val headerCredentials: ArrayList<ModelHeader>
        get() {
            val headerArrayList = ArrayList<ModelHeader>()
            if (MyApplication.isLoggedIn) {
                headerArrayList.add(ModelHeader("x-token", accessToken))
                headerArrayList.add(ModelHeader("x-userUID", userId))
            }
            return headerArrayList
        }

    val name: String?
        get() = sharedPreferences.getString("KEY_NAME", null)

    val email: String?
        get() = sharedPreferences.getString("KEY_EMAIL", null)

    var mobileNumber: String
        get() = sharedPreferences.getString(SHARED_NUMBER, "")!!
        set(value) = sharedPreferences.edit { putString(SHARED_NUMBER, value) }

    var userId: String
        get() = sharedPreferences.getString(SHARED_USER_ID, "")!!
        set(value) {
            sharedPreferences.edit().putString(SHARED_USER_ID, value).apply()
        }

    var accessToken: String?
        get() = sharedPreferences.getString(SHARED_TOKEN, null)
        set(value) {
            sharedPreferences.edit { putString(SHARED_TOKEN, value) }
        }

    @SuppressLint("CommitPrefEdits")
    fun createUserIdSession(token: String, userId: String) {
        this.userId = userId
        sharedPreferences.edit { putBoolean(SHARED_LOGIN_CHECK, true) }
        sharedPreferences.edit { putString(SHARED_TOKEN, token) }
        MyApplication.isLoggedIn = true
        MyApplication.instance.setAndRefreshVolleyHeaderCredentials()
    }

    fun logout() {
        sharedPreferences.edit().clear().apply()
        MyApplication.isLoggedIn = false
        MyApplication.instance.setAndRefreshVolleyHeaderCredentials()
    }

    fun getCompressions(): Int {
        return sharedPreferences.getInt("shared_compressions", 70);
    }

    val city: Int
        get() = sharedPreferences.getInt("KEY_CITY", 0)

    companion object {
        private const val SHARED_NAME = "gropix_storage"
        private const val SHARED_MODE = Context.MODE_PRIVATE
        private const val SHARED_LOGIN_CHECK = "shared_check_login"
        private const val SHARED_ADDRESS = "shared_address_data"
        private const val SHARED_TOKEN = "shared_key"
        private const val SHARED_USER_ID = "shared_user"
        private const val SHARED_NUMBER = "shared_number"
        private const val SHARED_SETTING_NOTIFICATION = "shared_notification"
        private const val SHARED_FCM_STATUS = "shared_fcm_sent"

        @JvmStatic
        @get:Synchronized
        var instance: UserStorage = UserStorage(MyApplication.appContext)

    }
}
