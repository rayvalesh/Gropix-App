package com.coagere.gropix.utils

import android.app.Activity
import com.coagere.gropix.utils.MyApplication
import com.coagere.gropix.R
import com.coagere.gropix.jetpack.repos.UserRepo
import com.tc.utils.variables.abstracts.OnEventOccurListener
import tk.jamun.ui.snacks.L

object HelperLogout {

    fun logMeOut(activity: Activity, listener: OnEventOccurListener?) {
        UserRepo().logout(object : OnEventOccurListener() {
            override fun onErrorResponse(`object`: Any, errorMessage: String) {
                super.onErrorResponse(`object`, errorMessage)
                listener?.getEventData(`object`)
                logMeOutHome(activity)
            }

            /**
             *  Method used to Remove the Current User Session by [UserRepo]
             *
             *  Checking Session and Setting Volley Headers [tk.jamun.volley.helpers.VolleyNeeds.headerArrayList]
             *
             */
            override fun getEventData(`object`: Any) {
                super.getEventData(`object`)
                listener?.getEventData(`object`)
                logMeOut(activity)
            }
        })
    }

    fun logMeOut(activity: Activity) {
        L.getInstance().toast(activity, activity.getString(R.string.string_toast_logout_success))
    }

    fun logMeOutHome(activity: Activity) {
        L.getInstance()
            .toast(activity, activity.getString(R.string.string_toast_logout_success))
    }
}
