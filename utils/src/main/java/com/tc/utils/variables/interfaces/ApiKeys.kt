package com.tc.utils.variables.interfaces

interface ApiKeys {
    companion object {

        const val URL_DOMAIN = "http://testing.techcruzers.com/grocery/bk/"
        const val URL_API = URL_DOMAIN + "api/"
        const val URL_PRIVACY_POLICY = ""

        const val URL_POST_DEVICE = URL_API + "user/deviceregistration"
        const val URL_POST_LOGIN = URL_API + "user/login"
        const val URL_GET_LOGOUT = URL_API + "user/logout"

        const val URL_POST_VERIFY_OTP = URL_API + "user/login/verification"
        const val URL_POST_RESEND_OTP = URL_API + "user/resend/otp"
        const val URL_POST_IMAGE_UPLOAD = URL_API + "user/upload/orderimage"

        const val URL_GET_ORDER_LIST = URL_API + "user/get/orderlist"
        const val URL_GET_ORDER_DETAILS = URL_API + "user/get/orderdetails?userOrderId="
        const val URL_POST_CREATE_ORDER = URL_API + "user/create/order"
        const val URL_POST_ORDER_CANCEL = URL_API + "user/cancelorder?userOrderId="
        const val URL_POST_ORDER_CONFIRM = URL_API + "user/confirmorder?userOrderId="


        /**
         * @param year : Year yyyy
         * @param month : JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER
         */

    }
}