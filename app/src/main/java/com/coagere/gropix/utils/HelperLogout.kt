package com.coagere.gropix.utils

import android.app.Activity
import android.content.Intent
import com.coagere.gropix.utils.MyApplication
import com.coagere.gropix.R
import com.coagere.gropix.jetpack.repos.UserRepo
import com.coagere.gropix.prefs.UserStorage
import com.coagere.gropix.ui.activities.AccessAccountActivity
import com.tc.utils.variables.abstracts.OnEventOccurListener
import tk.jamun.ui.snacks.L

object HelperLogout {

    fun logMeOut(activity: Activity, listener: OnEventOccurListener?) {
        UserRepo().logout(object : OnEventOccurListener() {
            override fun onErrorResponse(`object`: Any, errorMessage: String) {
                super.onErrorResponse(`object`, errorMessage)
                logMeOut(activity)
            }

            /**
             *  Method used to Remove the Current User Session by [UserRepo]
             *
             *  Checking Session and Setting Volley Headers [tk.jamun.volley.helpers.VolleyNeeds.headerArrayList]
             *
             */
            override fun getEventData(`object`: Any) {
                super.getEventData(`object`)
                logMeOut(activity)
            }
        })
    }

    fun logMeOut(activity: Activity) {
        L.getInstance().toast(activity, activity.getString(R.string.string_toast_logout_success))
        activity.finish()
        activity.startActivity(
            Intent(activity, AccessAccountActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        )
        UserStorage.instance.logout()
    }

}
