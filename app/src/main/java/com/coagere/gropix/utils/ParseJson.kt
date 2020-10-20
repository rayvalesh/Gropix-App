package com.coagere.gropix.utils


import com.coagere.gropix.prefs.TempStorage
import com.coagere.gropix.prefs.UserStorage
import com.tc.utils.utils.helpers.Utils
import com.tc.utils.variables.abstracts.OnEventOccurListener
import com.tc.utils.variables.enums.ResponseTypes
import org.json.JSONException
import org.json.JSONObject
import tk.jamun.volley.helpers.VolleyErrorExceptions


class ParseJson {

    fun dataCheck(jsonObject: JSONObject?, key: String): Boolean {
        return jsonObject != null && jsonObject.has(key) && !jsonObject.isNull(key)
    }

    private fun instanceOfJSONObject(response: Any?): Boolean {
        return response is JSONObject
    }


    fun parseResponseStatus(response: Any?, listener: OnEventOccurListener?) {
        try {
            val jsonObject = response as JSONObject
            if (isResponseTrue(jsonObject)) {
                listener?.getEventData(ResponseTypes.TYPE_SUCCESS)
            } else {
                parseErrorMessageResponse(jsonObject, listener)
            }
        } catch (ignored: JSONException) {
        }
    }

    private fun isResponseTrue(jsonObject: JSONObject): Boolean {
        return true

    }


    private fun parseErrorMessageResponse(
        jsonObject: JSONObject,
        listener: OnEventOccurListener?
    ) {
        if (dataCheck(jsonObject, "message")) {
            listener?.onErrorResponse(
                ResponseTypes.TYPE_ERROR,
                jsonObject.getString("message")
            )
        } else {
            listener?.onErrorResponse(
                ResponseTypes.TYPE_ERROR,
                VolleyErrorExceptions.get().VOLLEY_ERROR_STRING_CHECK_DATA
            )
        }
    }


    fun parseOtpVerification(response: Any?, listener: OnEventOccurListener) {
        if (instanceOfJSONObject(response)) {
            try {
                val jsonObject = response as JSONObject
                if (dataCheck(jsonObject, "token")) {
                    UserStorage.instance.createUserIdSession(
                        jsonObject.getString("token"),
                        UserStorage.instance.userId
                    )
                    listener.getEventData(ResponseTypes.TYPE_SUCCESS)
                } else {
                    parseErrorMessageResponse(jsonObject, listener)
                }
            } catch (ignored: JSONException) {
                listener.onErrorResponse(
                    ResponseTypes.TYPE_ERROR,
                    VolleyErrorExceptions.get().VOLLEY_ERROR_STRING_PARSING
                )
            }
        }
    }

    fun parseDeviceRegisterResponse(response: Any, listener: OnEventOccurListener) {
        if (instanceOfJSONObject(response)) {
            try {
                val jsonObject = response as JSONObject
                if (dataCheck(jsonObject, "deviceAppId")) {
                    TempStorage.instance.appId = jsonObject.getInt("deviceAppId")
                    listener.getEventData(ResponseTypes.TYPE_SUCCESS)
                } else {
                    parseErrorMessageResponse(jsonObject, listener)
                }
            } catch (ignored: JSONException) {
                listener.onErrorResponse(
                    ResponseTypes.TYPE_ERROR,
                    VolleyErrorExceptions.get().VOLLEY_ERROR_STRING_PARSING
                )
            }
        }
    }

    fun parseLoginResponse(response: Any?, listener: OnEventOccurListener) {
        if (instanceOfJSONObject(response)) {
            try {
                val jsonObject = response as JSONObject
                if (dataCheck(jsonObject, "userUID")) {
                    UserStorage.instance.userId = jsonObject.getString("userUID")
                    listener.getEventData(ResponseTypes.TYPE_SUCCESS)
                } else {
                    parseErrorMessageResponse(jsonObject, listener)
                }
            } catch (ignored: JSONException) {
                listener.onErrorResponse(
                    ResponseTypes.TYPE_ERROR,
                    VolleyErrorExceptions.get().VOLLEY_ERROR_STRING_PARSING
                )
            }
        }
    }


    companion object {
        @JvmStatic
        @get:Synchronized
        @Volatile
        var instance: ParseJson = ParseJson()

    }
}
