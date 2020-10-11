package com.coagere.gropix.jetpack.repos

import com.android.volley.Request
import com.android.volley.VolleyError
import com.coagere.gropix.R
import com.coagere.gropix.jetpack.entities.ContactModel
import com.coagere.gropix.prefs.TempStorage
import com.coagere.gropix.prefs.UserStorage
import com.coagere.gropix.utils.MyApplication
import com.coagere.gropix.utils.ParseJson
import com.coagere.gropix.utils.VolleySolutions
import com.google.gson.reflect.TypeToken
import com.tc.utils.utils.helpers.DeviceInfo
import com.tc.utils.utils.helpers.Utils
import com.tc.utils.variables.abstracts.OnEventOccurListener
import com.tc.utils.variables.interfaces.ApiKeys
import com.tc.utils.variables.interfaces.ApiKeys.Companion.URL_GET_LOGOUT
import com.tc.utils.variables.interfaces.ApiKeys.Companion.URL_POST_LOGIN
import com.tc.utils.variables.interfaces.ApiKeys.Companion.URL_POST_RESEND_OTP
import com.tc.utils.variables.interfaces.ApiKeys.Companion.URL_POST_VERIFY_OTP
import org.json.JSONException
import org.json.JSONObject
import tk.jamun.volley.classes.VolleyGSON
import tk.jamun.volley.classes.VolleyJsonObjectRequest
import tk.jamun.volley.helpers.VolleyNeeds
import tk.jamun.volley.variables.VolleyResponses

class UserRepo {
    private var volleyJsonObjectRequest: VolleyJsonObjectRequest? = null

    fun apiPostDeviceByApi(listener: OnEventOccurListener) {
        val jsonObject = JSONObject()
        try {
            val deviceInfo = DeviceInfo()
            jsonObject.put("mobileDeviceId", deviceInfo.getDeviceId(MyApplication.appContext))
            jsonObject.put("modelName", deviceInfo.deviceName)
            jsonObject.put(
                "appVersion",
                MyApplication.appContext.getString(R.string.string_app_version)
            )
            jsonObject.put("osVersion", deviceInfo.androidVersion)
        } catch (ignored: JSONException) {
        }
        volleyJsonObjectRequest = VolleyJsonObjectRequest(
            Request.Method.POST,
            ApiKeys.URL_POST_DEVICE,
            jsonObject.toString(),
            object : VolleyResponses() {
                override fun onResponse(response: Any?, body: String?) {
                    super.onResponse(response, body)
                    ParseJson.instance.parseDeviceRegisterResponse(response!!, listener)
                }

                override fun onErrorResponse(
                    error: VolleyError?, statusCode: Int,
                    errorMessage: String?
                ) {
                    super.onErrorResponse(error, statusCode, errorMessage)
                    listener.onErrorResponse(statusCode, errorMessage)
                }
            })
        VolleyNeeds.get().addCalls(volleyJsonObjectRequest!!)
    }


    fun apiAccessAccount(number: String, listener: OnEventOccurListener) {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("countryCode", "91")
            jsonObject.put("mobileNumber", number)
            jsonObject.put("deviceAppId", TempStorage.instance.appId)
        } catch (ignored: JSONException) {
        }
        Utils.log(jsonObject.toString())
        volleyJsonObjectRequest =
            VolleyJsonObjectRequest(
                URL_POST_LOGIN,
                jsonObject.toString(),
                object : VolleyResponses() {
                    override fun onResponse(response: Any?, body: String?) {
                        super.onResponse(response, body)
                        ParseJson.instance.parseLoginResponse(response, listener)
                    }

                    override fun onErrorResponse(statusCode: Int, errorMessage: String?) {
                        super.onErrorResponse(statusCode, errorMessage)
                        Utils.log(errorMessage)
                        listener.onErrorResponse(statusCode, errorMessage)
                    }
                })
        VolleyNeeds.get().addCalls(volleyJsonObjectRequest!!)
    }

    fun apiResendOtp(listener: OnEventOccurListener) {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("userUID", UserStorage.instance.userId)
        } catch (ignored: JSONException) {
        }
        volleyJsonObjectRequest = VolleyJsonObjectRequest(
            URL_POST_RESEND_OTP,
            jsonObject.toString(),
            object : VolleyResponses() {
                override fun onResponse(response: Any?, body: String?) {
                    super.onResponse(response, body)
                    ParseJson.instance.parseLoginResponse(response, listener)
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
            jsonObject.put("userUID", UserStorage.instance.userId)
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

    fun postFcm() {
        val jsonObject = JSONObject()
        jsonObject.put("fcm", TempStorage.instance.fcmToken)
        VolleySolutions.instance.postCommonTasks(ApiKeys.URL_POST_FCM, jsonObject.toString(),
            object : OnEventOccurListener() {
                override fun getEventData(`object`: Any?) {
                    super.getEventData(`object`)
                    TempStorage.instance.isFcmSent = true
                }
            })
    }

    fun postFeedback(model: ContactModel, listener: OnEventOccurListener) {
        VolleySolutions.instance.postCommonTasks(
            ApiKeys.URL_POST_FEEDBACK,
            VolleyGSON.get().toJson(
                model,
                VolleyGSON.get().getTypeToken(object : TypeToken<ContactModel>() {})
            ),
            listener
        )
    }

    fun logout(listener: OnEventOccurListener) {
        VolleySolutions.instance.postCommonTasks(URL_GET_LOGOUT, null, listener)
    }


    companion object {
        @get:Synchronized
        val instance = UserRepo()
    }
}
