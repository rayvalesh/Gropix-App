package com.tc.utils.variables.interfaces

interface ApiKeys {
    companion object {

        const val URL_FACEBOOK = "https://www.facebook.com/KidExIndia/"
        const val KEY_LATITUDE = "latitude"
        const val KEY_LONGITUDE = "longitude"
        const val URL_LINKEDIN = "https://www.linkedin.com/company/kidex/"
        const val URL_INSTAGRAM = "https://www.instagram.com/kidex2020/"
        const val URL_LOCATION =
            "https://www.google.com/maps/place/KidEx/@28.4507132,77.0446653,15z/data=!4m5!3m4!1s0x0:0xe1df83a98970b3dc!8m2!3d28.4507132!4d77.0446653"


        const val VALUE_PAGE_SIZE = 20
        const val URL_API = "http://dev-api.kid-ex.com/api/"
        const val URL_TERMS = ""
        const val URL_PRIVACY_POLICY = ""
        const val URL_POST_LOGIN = URL_API + "otp/generate"
        const val URL_POST_VERIFY_OTP = URL_API + "otp/validate?mobile_no="
        const val URL_GET_NOTIFICATION = URL_API + "notification"
        const val URL_GET_ACTIVITIES = URL_API + "activity/all"
        const val URL_GET_CONFIG = URL_API + "config/enums"


        /**
         * @param year : Year yyyy
         * @param month : JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER
         */
        const val URL_GET_SCHEDULE = URL_API + "schedule/monthly?month="
        const val URL_GET_STUDENT = URL_API + "student/all"
        const val URL_GET_ATTENDANCE = URL_API + "attendance"
        const val URL_GET_ASSESSMENT = URL_API + "assessment?student_id="
        const val URL_GET_CURRICULUM = URL_API + "admin/curriculum?activity_name="
        const val URL_GET_LEAVES = URL_API + "notification"
        const val URL_GET_AVAILABILITY = URL_API + "notification"
        const val URL_GET_REFER = URL_API + "notification"
        const val URL_GET_INSTRUCTION = URL_API + "class/instructions?class_id="
        const val URL_GET_CLASS = URL_API + "schedule?pageSize=${VALUE_PAGE_SIZE}&pageNumber="
        const val URL_GET_CLASS_TOPIC_COVERED = URL_API + "class/topics_covered?class_id="
        const val URL_GET_PROFILE = URL_API + "users/coach"

        const val URL_POST_CLASS = URL_API + "schedule"
        const val URL_POST_CLASS_EDIT = URL_API + "class/edit"
        const val URL_POST_CLASS_LOG = URL_API + "class/log"
        const val URL_POST_STUDENT = URL_API + "student"
        const val URL_POST_ATTENDANCE = URL_API + "attendance"
        const val URL_POST_ASSESSMENT = URL_API + "assessment"
        const val URL_PUT_USER = URL_API + "users/coach/update"
        const val URL_POST_FCM: String = URL_API + "users/register_fcm?token="
        const val URL_POST_REFER = URL_API + ""
        const val URL_POST_INSTRUCTION = URL_API + "instructions"
        const val URL_PUT_REFER = URL_API + ""
        const val URL_POST_LEAVES = URL_API + ""
        const val URL_PUT_LEAVES = URL_API + ""
        const val URL_POST_AVAILABILITY = URL_API + ""
        const val URL_PUT_AVAILABILITY = URL_API + ""
        const val URL_DELETE_AVAILABILITY = URL_API + ""
        const val URL_DELETE_LEAVES = URL_API + ""
        const val URL_POST_CURRICULUM = URL_API + ""
        const val URL_POST_CONTACT = URL_API + ""
        const val URL_POST_UPLOAD_PROFILE_PHOTO =
            "http://dev-api.kid-ex.com/upload/get-presigned-url?file_type=Image&upload_type=PROFILE"
        const val URL_POST_LOGOUT = URL_API + "users/logout"

        const val URL_DELETE_PHOTO = URL_API + "users/profile_photo/delete"
    }
}