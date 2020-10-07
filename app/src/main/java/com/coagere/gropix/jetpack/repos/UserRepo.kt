package com.coagere.gropix.jetpack.repos

import com.bumptech.glide.util.Util
import com.coagere.gropix.jetpack.entities.ContactModel
import com.coagere.gropix.jetpack.entities.UserModel
import com.coagere.gropix.utils.ParseJson
import com.tc.utils.utils.helpers.Utils
import com.tc.utils.variables.abstracts.OnEventOccurListener
import com.tc.utils.variables.interfaces.ApiKeys.Companion.URL_POST_LOGIN
import com.tc.utils.variables.interfaces.ApiKeys.Companion.URL_POST_RESEND_OTP
import com.tc.utils.variables.interfaces.ApiKeys.Companion.URL_POST_VERIFY_OTP
import org.json.JSONException
import org.json.JSONObject
import tk.jamun.volley.classes.VolleyJsonObjectRequest
import tk.jamun.volley.helpers.VolleyNeeds
import tk.jamun.volley.variables.VolleyResponses

class UserRepo {
    private var volleyJsonObjectRequest: VolleyJsonObjectRequest? = null

    fun apiAccessAccount(number: String, listener: OnEventOccurListener) {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("number", number)
        } catch (ignored: JSONException) {
        }
        volleyJsonObjectRequest =
            VolleyJsonObjectRequest(
                URL_POST_LOGIN,
                UserModel::class.java,
                object : VolleyResponses() {
                    override fun onResponse(response: Any?, body: String?) {
                        super.onResponse(response, body)
                        ParseJson.instance.parseResponseStatus(response, listener)
                    }

                    override fun onErrorResponse(statusCode: Int, errorMessage: String?) {
                        super.onErrorResponse(statusCode, errorMessage)
                        listener.onErrorResponse(statusCode, errorMessage)
                    }
                })
        VolleyNeeds.get().addCalls(volleyJsonObjectRequest!!)
    }

    fun apiResendOtp(number: String, listener: OnEventOccurListener) {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("number", number)
        } catch (ignored: JSONException) {
        }
        volleyJsonObjectRequest = VolleyJsonObjectRequest(
            URL_POST_RESEND_OTP,
            jsonObject.toString(),
            object : VolleyResponses() {
                override fun onResponse(response: Any?, body: String?) {
                    super.onResponse(response, body)
                    ParseJson.instance.parseResponseStatus(response, listener)
                }

                override fun onErrorResponse(statusCode: Int, errorMessage: String?) {
                    super.onErrorResponse(statusCode, errorMessage)
                    listener.onErrorResponse(statusCode, errorMessage)
                }
            })
        VolleyNeeds.get().addCalls(volleyJsonObjectRequest!!)
    }


    fun apiOtpVerification(number: String, otp: String, listener: OnEventOccurListener) {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("number", number)
            jsonObject.put("otp", otp)
        } catch (ignored: JSONException) {
        }
        volleyJsonObjectRequest = VolleyJsonObjectRequest(URL_POST_VERIFY_OTP,
            jsonObject.toString(), object : VolleyResponses() {
                override fun onResponse(response: Any?, body: String?) {
                    super.onResponse(response, body)
                    ParseJson.instance.parseOtpVerification(response, listener)
                }

                override fun onErrorResponse(statusCode: Int, errorMessage: String?) {
                    super.onErrorResponse(statusCode, errorMessage)
                    listener.onErrorResponse(statusCode, errorMessage)
                }
            })
        VolleyNeeds.get().addCalls(volleyJsonObjectRequest!!)
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
