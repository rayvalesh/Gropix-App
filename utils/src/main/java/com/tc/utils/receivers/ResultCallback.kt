package com.tc.utils.receivers

import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import androidx.annotation.Keep

/**
 * [ResultCallback] class help you implementing [onActivityResult] feature implementation to get data from B to A
 */
@Keep
class ResultCallback : ResultReceiver {
    private var receiver: Receiver?
    private var requestCode = 0

    constructor(handler: Handler?, receiver: Receiver?) : super(handler) {
        this.receiver = receiver
    }

    constructor(handler: Handler?, receiver: Receiver?, requestCode: Int) : super(handler) {
        this.receiver = receiver
        this.requestCode = requestCode
    }

    interface Receiver {


        /**
         * Fun provide data coming from other parties
         *
         * @param requestCode Integer value uniquely identify
         * @param resultCode [Activity.RESULT_OK] or [Activity.RESULT_CANCEL]
         * @param resultData [Bundle] value for data
         */
        fun onReceiveResult(requestCode: Int, resultCode: Int, resultData: Bundle?)
    }

    /**
     * Call back function call to get Response from B to A
     * @param requestCode Integer value uniquely identify
     * @param resultData [Bundle] value for data
     */
    override fun onReceiveResult(resultCode: Int, resultData: Bundle) {
        if (receiver != null) {
            receiver?.onReceiveResult(requestCode, resultCode, resultData)
        }
    }
}