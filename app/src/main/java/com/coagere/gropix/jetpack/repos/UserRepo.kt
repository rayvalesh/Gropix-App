package com.coagere.gropix.jetpack.repos

import com.coagere.gropix.jetpack.entities.ContactModel
import com.coagere.gropix.jetpack.entities.UserModel
import com.tc.utils.variables.abstracts.OnEventOccurListener
import tk.jamun.volley.classes.VolleyJsonObjectRequest

class UserRepo {
    private var commonVolleyJsonObjectRequest: VolleyJsonObjectRequest? = null
    private var volleyJsonObjectRequest: VolleyJsonObjectRequest? = null

    fun apiAccessAccount(number: String, listener: OnEventOccurListener) {

    }

    fun apiResendOtp(number: String, listener: OnEventOccurListener) {

    }


    fun apiOtpVerification(number: String, otp: String, listener: OnEventOccurListener) {

    }


    fun apiGetProfileData(listener: OnEventOccurListener) {

    }


    fun apiUpdateProfile(model: UserModel, referralCode: String?, listener: OnEventOccurListener) {

    }

    fun apiUpdateFCM() {

    }


    fun postFeedback(model: ContactModel, listener: OnEventOccurListener) {
    }

    fun logout(listener: OnEventOccurListener) {
//        VolleySolutions.instance.postCommonTasks(URL_GET_LOGOUT, null, listener)
    }

    fun apiGetCommonData(onEventOccurListener: OnEventOccurListener) {
        TODO("Not yet implemented")
    }


    companion object {
        @get:Synchronized
        val instance = UserRepo()
    }
}
