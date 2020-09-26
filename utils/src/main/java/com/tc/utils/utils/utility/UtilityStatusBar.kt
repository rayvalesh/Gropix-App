package com.tc.utils.utils.utility

import android.app.Activity
import androidx.core.content.ContextCompat
import com.tc.utils.utils.helpers.CheckOs.checkBuildForLolipop

object UtilityStatusBar {
    fun changeStatusBarColor(activity: Activity, color: Int) {
        if (checkBuildForLolipop()) {
            val window = activity.window
            window.statusBarColor = ContextCompat.getColor(activity, color)
        }
    }
}