package com.coagere.gropix.utils

import com.android.volley.Request
import com.android.volley.VolleyError


import com.tc.utils.utils.helpers.Utils
import com.tc.utils.utils.utility.isNotNull

import com.tc.utils.variables.abstracts.OnEventOccurListener

import org.json.JSONObject
import tk.jamun.ui.snacks.L

import tk.jamun.volley.classes.VolleyJsonObjectRequest
import tk.jamun.volley.helpers.VolleyNeeds
import tk.jamun.volley.variables.VolleyResponses

class VolleySolutions {
    private var objectRequest: Any? = null

    fun postCommonTasks(
        url: String,
        data: String? = null,
        listener: OnEventOccurListener? = null
    ) {
        doCommonTasks(Request.Method.POST, url, data, listener)
    }

    fun putCommonTasks(
        url: String,
        data: String? = null,
        listener: OnEventOccurListener? = null
    ) {
        doCommonTasks(Request.Method.PUT, url, data, listener)
    }

    fun deleteCommonTasks(
        url: String,
        data: String? = null,
        listener: OnEventOccurListener? = null
    ) {
        doCommonTasks(Request.Method.DELETE, url, data, listener)
    }


    fun doCommonTasks(
        method: Int = Request.Method.GET,
        url: String,
        data: String? = null,
        listener: OnEventOccurListener? = null
    ) {
        objectRequest = VolleyJsonObjectRequest(method, url, data,
            object : VolleyResponses() {
            override fun onResponse(response: Any?, body: String?) {
                super.onResponse(response, body)
                ParseJson.instance.parseResponseStatus(response, listener)

            }
            override fun onErrorResponse(error: VolleyError?, statusCode: Int, errorMessage: String?) {
                super.onErrorResponse(error, statusCode, errorMessage)
                if (isNotNull(listener))
                    listener?.onErrorResponse(statusCode, errorMessage)
            }
        })
        VolleyNeeds.get().addCalls(objectRequest!!)
    }

    fun getObject(): Any? {
        return objectRequest
    }


    companion object {
        @get:Synchronized
        @Volatile
        var instance: VolleySolutions = VolleySolutions()
    }
}
