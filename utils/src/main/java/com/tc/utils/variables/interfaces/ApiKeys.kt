package com.tc.utils.variables.interfaces

interface ApiKeys {
    companion object {

        const val URL_DOMAIN = "http://gropix.in/bk/"
        const val URL_API = URL_DOMAIN + "api/user/"
        const val URL_PRIVACY_POLICY = "http://www.gropix.in/pp/privacy-policy.html"

        const val URL_POST_DEVICE = URL_API + "deviceregistration"
        const val URL_POST_LOGIN = URL_API + "login"
        const val URL_GET_LOGOUT = URL_API + "logout"

        const val URL_POST_VERIFY_OTP = URL_API + "login/verification"
        const val URL_POST_RESEND_OTP = URL_API + "resend/otp"
        const val URL_POST_IMAGE_UPLOAD = URL_API + "upload/orderimage"

        const val URL_GET_ORDER_LIST = URL_API + "get/orderlist"
        const val URL_GET_ORDER_DETAILS = URL_API + "get/orderdetails?userOrderId="
        const val URL_POST_CREATE_ORDER = URL_API + "create/order"
        const val URL_POST_ORDER_CANCEL = URL_API + "cancel/order"
        const val URL_POST_ORDER_CONFIRM = URL_API + "confirm/order"
        const val URL_POST_FEEDBACK = URL_API + "post/feedback"
        const val URL_PUT_FCM = URL_API + "update/fcm"
    }
}