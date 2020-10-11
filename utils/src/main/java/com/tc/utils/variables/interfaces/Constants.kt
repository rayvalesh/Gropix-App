package com.tc.utils.variables.interfaces

interface Constants {
    companion object {
        const val VERSION_CODE = 1
        const val SHARED_LAUNCHER_VERSION = 1
        const val SPACE = " "

        const val THREAD_TIME_DELAY: Long = 90
        const val VALUE_YES = 1

        //1. Pending, 2 Confirmed, 3. Bill Upload, 4 Out for deliver. 5. Complete, 6. Cancelled
        const val ORDER_PENDING = 1
        const val ORDER_CART = 2
        const val ORDER_CONFIRMED = 3
        const val ORDER_OUT_FOR_DELIVERY = 4
        const val ORDER_COMPLETE = 5
        const val ORDER_CANCELLED = 6

        const val CONSTANTS_IMAGES_COUNT = 8
        const val VALUE_IMAGE_RADIUS = 22

        const val RESPONSE_PENDING = 1
        const val RESPONSE_SUCCESS = 2
        const val TYPE_ACTION_ADD = 1
        const val TYPE_ACTION_UPLOAD = 3
        const val TYPE_ACTION_CANCEL = 4
        const val TYPE_UPLOAD_STOP = 5
        const val TYPE_ACTION_DELETE = 11

        const val FILE_SIZE_LIMIT = 12 * 1024 * 1024;
    }


}