package com.coagere.gropix.utils


import com.tc.utils.variables.abstracts.OnEventOccurListener
import com.tc.utils.variables.enums.ResponseTypes
import com.tc.utils.variables.interfaces.Constants
import org.json.JSONException
import org.json.JSONObject
import tk.jamun.volley.helpers.VolleyErrorExceptions


class ParseJson {

    fun dataCheck(jsonObject: JSONObject?, key: String): Boolean {
        return jsonObject != null && jsonObject.has(key) && !jsonObject.isNull(key)
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


    }


    companion object {
        @JvmStatic
        @get:Synchronized
        @Volatile
        var instance: ParseJson = ParseJson()

    }
}
