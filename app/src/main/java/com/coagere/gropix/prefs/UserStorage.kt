package com.coagere.gropix.prefs

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.coagere.gropix.jetpack.entities.UserModel
import com.coagere.gropix.utils.MyApplication
import tk.jamun.volley.model.ModelHeader
import java.util.*

class UserStorage(context: Context) {

    val isAdmin: Boolean
        get() = sharedPreferences.getBoolean("isAdmin", false)

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(SHARED_NAME, SHARED_MODE)

    var isDoNotDisturbEnabled: Boolean
        get() = sharedPreferences.getBoolean(SHARED_SETTING_NOTIFICATION_DO_NOT_DISTURB, false)
        set(value) = sharedPreferences.edit {
            putBoolean(
                SHARED_SETTING_NOTIFICATION_DO_NOT_DISTURB,
                value
            )
        }

    var isNotificationEnabled: Boolean
        get() = sharedPreferences.getBoolean(SHARED_SETTING_NOTIFICATION, true)
        set(value) = sharedPreferences.edit { putBoolean(SHARED_SETTING_NOTIFICATION, value) }


    var isFirstTime: Boolean
        get() = sharedPreferences.getBoolean("shared_user_first_time", true)
        set(value) = sharedPreferences.edit { putBoolean("shared_user_first_time", value) }

    val isLoggedIn: Boolean
        get() = sharedPreferences.getBoolean(SHARED_LOGIN_CHECK, false)

    var hasSubscription: Boolean
        get() = sharedPreferences.getBoolean(SHARED_SUBSCRIPTION, true)
        set(value) {
            sharedPreferences.edit().putBoolean(SHARED_SUBSCRIPTION, value).apply()
        }
    val headerCredentials: ArrayList<ModelHeader>
        get() {
            val headerArrayList = ArrayList<ModelHeader>()
            headerArrayList.add(ModelHeader("appkey", "3a3848c7-1327-4f2f-989a-3b52d3121477"))
            if (MyApplication.isLoggedIn) {
                headerArrayList.add(ModelHeader("user", userId))
                headerArrayList.add(ModelHeader("key", accessToken))
                if (courseId != 0)
                    headerArrayList.add(ModelHeader("course-id", courseId.toString()))
            }
            return headerArrayList
        }

    val name: String?
        get() = sharedPreferences.getString("KEY_NAME", null)

    val email: String?
        get() = sharedPreferences.getString("KEY_EMAIL", null)

    val displayName: String?
        get() = sharedPreferences.getString(SHARED_DISPLAY, null)

    val displayImage: String?
        get() = sharedPreferences.getString(SHARED_DISPLAY_IMAGE, null)


    var mobileNumber: String
        get() = sharedPreferences.getString(SHARED_NUMBER, "")!!
        set(value) = sharedPreferences.edit { putString(SHARED_NUMBER, value) }

    var lastPage: Int
        get() = sharedPreferences.getInt(SHARED_PAGE, 0)
        set(page) {
            sharedPreferences.edit().putInt(SHARED_PAGE, page).apply()
        }

    var savedUpdateDialogValue: String?
        get() = sharedPreferences.getString(SHARED_DIALOG_UPDATE, null)
        set(value) {
            sharedPreferences.edit { putString(SHARED_DIALOG_UPDATE, value) }
        }

    var userId: String
        get() = sharedPreferences.getString(SHARED_USER_ID, "")!!
        set(value) {
            sharedPreferences.edit().putString(SHARED_USER_ID, value).apply()
        }

    var referralCode: String?
        get() = sharedPreferences.getString(SHARED_REFERRAL, null)
        set(value) {
            sharedPreferences.edit().putString(SHARED_REFERRAL, value).apply()
        }
    var deepReferralCode: String?
        get() = sharedPreferences.getString("shared_referral_deep", null)
        set(value) {
            sharedPreferences.edit().putString("shared_referral_deep", value).apply()
        }

    var deepPostCode: String?
        get() = sharedPreferences.getString("shared_post_deep", null)
        set(value) {
            sharedPreferences.edit().putString("shared_post_deep", value).apply()
        }


    var deepTeacherCode: String?
        get() = sharedPreferences.getString("shared_teacher_deep", null)
        set(value) {
            sharedPreferences.edit().putString("shared_teacher_deep", value).apply()
        }

    private var accessToken: String?
        get() = sharedPreferences.getString(SHARED_TOKEN, null)
        set(value) {
            sharedPreferences.edit { putString(SHARED_TOKEN, value) }
        }

    var messageUUID: String?
        get() = sharedPreferences.getString(SHARED_UUID_MESSAGE, null)
        set(value) = sharedPreferences.edit { putString(SHARED_UUID_MESSAGE, value) }

    fun setUserProfile(userModel: UserModel) {
        sharedPreferences.edit()
            ?.putString(SHARED_DISPLAY, userModel.displayName)
            ?.putString(
                SHARED_DISPLAY_IMAGE,
                if (userModel.userImage.isNullOrEmpty()) "" else userModel.userImage
            )
            ?.apply()
    }

    fun getUserProfile(): UserModel {
        val userModel = UserModel()
        userModel.displayName = sharedPreferences.getString(SHARED_DISPLAY, null)
        userModel.userImage = sharedPreferences.getString(SHARED_DISPLAY_IMAGE, null)
        return userModel
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

    var navigationStatus: Boolean
        get() = sharedPreferences.getBoolean(SHARED_NAVIGATION, false)
        set(value) = sharedPreferences.edit().putBoolean(SHARED_NAVIGATION, value).apply()


    var classes: Int
        get() = sharedPreferences.getInt(SHARED_CLASS, 0)
        set(value) {
            sharedPreferences.edit().putInt(SHARED_CLASS, value).apply()
        }
    var boards: Int
        get() = sharedPreferences.getInt(SHARED_BOARDS, 0)
        set(value) {
            sharedPreferences.edit().putInt(SHARED_BOARDS, value).apply()
        }

    var accountStatus: Int
        get() = sharedPreferences.getInt(SHARED_ACCOUNT_STATUS, 1)
        set(value) {
            sharedPreferences.edit().putInt(SHARED_ACCOUNT_STATUS, value).apply()
        }

    var courseId: Int
        get() = sharedPreferences.getInt("shared_course_id", 0)
        set(value) {
            sharedPreferences.edit().putInt("shared_course_id", value).apply()
        }

    val city: Int
        get() = sharedPreferences.getInt("KEY_CITY", 0)

    var fcmSentStatus: Boolean
        get() = sharedPreferences.getBoolean(SHARED_FCM_STATUS, false)
        set(value) = sharedPreferences.edit().putBoolean(SHARED_FCM_STATUS, value).apply()

    var firstCodeGuiderDone: Boolean
        get() = sharedPreferences.getBoolean("shared_code_guider", false)
        set(value) = sharedPreferences.edit().putBoolean("shared_code_guider", value).apply()

    var homeGuiderDone: Boolean
        get() = sharedPreferences.getBoolean("shared_home_guider", false)
        set(value) = sharedPreferences.edit().putBoolean("shared_home_guider", value).apply()

    var videoPlayerGuiderDone: Boolean
        get() = sharedPreferences.getBoolean("shared_video_player_guider", false)
        set(value) = sharedPreferences.edit().putBoolean("shared_video_player_guider", value)
            .apply()

    var tabGuiderDone: Boolean
        get() = sharedPreferences.getBoolean("shared_tab_guider", false)
        set(value) = sharedPreferences.edit().putBoolean("shared_tab_guider", value).apply()

    companion object {
        private const val SHARED_NAME = "studiuz_storage"
        private const val SHARED_MODE = Context.MODE_PRIVATE
        private const val SHARED_PAGE = "shared_page"
        private const val SHARED_SUBSCRIPTION = "shared_has_subscription"
        private const val SHARED_LOGIN_CHECK = "shared_check_login"
        private const val SHARED_TEACHERS_ID = "shared_teacher_data"
        private const val SHARED_TOKEN = "shared_key"
        private const val SHARED_USER_ID = "shared_user"
        private const val SHARED_REFERRAL = "shared_referral"
        private const val SHARED_CLASS = "shared_classes"
        private const val SHARED_NUMBER = "shared_number"
        private const val SHARED_DISPLAY = "shared_display"
        private const val SHARED_DISPLAY_IMAGE = "shared_user_image"
        private const val SHARED_NAVIGATION = "shared_navigation"
        private const val SHARED_BOARDS = "shared_boards"
        private const val SHARED_ACCOUNT_STATUS = "account_status"
        private const val SHARED_UUID_MESSAGE = "shared_uuid_message"
        private const val SHARED_DIALOG_UPDATE = "shared_update_dialog"
        private const val SHARED_SETTING_NOTIFICATION_DO_NOT_DISTURB = "shared_dnd"
        private const val SHARED_SETTING_NOTIFICATION = "shared_notification"
        private const val SHARED_FCM_STATUS = "shared_fcm_sent"

        @JvmStatic
        @get:Synchronized
        var instance: UserStorage = UserStorage(MyApplication.appContext)

    }
}
