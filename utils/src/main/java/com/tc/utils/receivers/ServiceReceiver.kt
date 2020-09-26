package com.tc.utils.receivers

import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver

class ServiceReceiver : ResultReceiver {
    private var receiver: Receiver? = null
    private var requestCode: Int = 0

    constructor(handler: Handler, receiver: Receiver) : super(handler) {
        this.receiver = receiver
    }

    constructor(handler: Handler, receiver: Receiver, requestCode: Int) : super(handler) {
        this.receiver = receiver
        this.requestCode = requestCode
    }

    interface Receiver {
        fun onReceiveResult(requestCode: Int, resultCode: Int, resultData: Bundle)
    }

    override fun onReceiveResult(resultCode: Int, resultData: Bundle) {
        if (receiver != null) {
            receiver!!.onReceiveResult(requestCode, resultCode, resultData)
        }
    }

    companion object {
        const val STATUS_FINISHED = 1
        const val STATUS_ERROR = 2
        const val TAG_RECEIVER = "RECEIVER"
        const val RESULT = "RESULT"
    }
}
