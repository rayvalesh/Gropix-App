package com.tc.utils.variables.interfaces

interface ApiKeys {
    companion object {

        const val VALUE_PAGE_SIZE = 20

        const val URL_API = "http://testing.techcruzers.com/grocery/bk/api/"
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


        /**
         * @param year : Year yyyy
         * @param month : JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER
         */

    }
}