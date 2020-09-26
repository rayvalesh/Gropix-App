package com.coagere.gropix.utils

import android.content.Context

class ShareData(private val activity: Context) {

    //
    val referralShareDesc: String
        get() = ("\n \n"
                + "https://play.google.com/store/apps/details?id=" + activity.packageName + "")

    val referralShareSubject: String
        get() = "GroPix"
}
