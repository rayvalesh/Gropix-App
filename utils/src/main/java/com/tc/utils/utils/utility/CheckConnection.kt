package com.tc.utils.utils.utility

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.tc.utils.R
import tk.jamun.ui.snacks.MySnackBar


object CheckConnection {

    fun checkConnection(activity: Activity? = null): Boolean {
        if (isNotNull(activity)) {
            val cm = activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo?
            activeNetwork = cm.activeNetworkInfo
            if (activeNetwork != null && activeNetwork.isConnected) {
                return true
            } else {
                MySnackBar.getInstance().showSnackBarForMessage(activity, R.string.connection_check_no_internet)
            }
        }
        return false
    }

    fun checkCon(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo?
        activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }
}
