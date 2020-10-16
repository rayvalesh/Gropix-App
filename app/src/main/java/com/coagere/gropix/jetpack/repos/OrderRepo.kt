package com.coagere.gropix.jetpack.repos

import com.coagere.gropix.jetpack.entities.OrderModel
import com.coagere.gropix.utils.ParseJson
import com.coagere.gropix.utils.VolleySolutions
import com.tc.utils.utils.helpers.Utils
import com.tc.utils.variables.abstracts.OnEventOccurListener
import com.tc.utils.variables.enums.ResponseTypes
import com.tc.utils.variables.interfaces.ApiKeys.Companion.URL_GET_ORDER_DETAILS
import com.tc.utils.variables.interfaces.ApiKeys.Companion.URL_GET_ORDER_LIST
import com.tc.utils.variables.interfaces.ApiKeys.Companion.URL_POST_CREATE_ORDER
import com.tc.utils.variables.interfaces.ApiKeys.Companion.URL_POST_ORDER_CANCEL
import com.tc.utils.variables.interfaces.ApiKeys.Companion.URL_POST_ORDER_CONFIRM
import org.json.JSONArray
import org.json.JSONObject
import tk.jamun.ui.snacks.L
import tk.jamun.volley.classes.VolleyJsonArrayRequest
import tk.jamun.volley.classes.VolleyJsonObjectRequest
import tk.jamun.volley.helpers.VolleyErrorExceptions
import tk.jamun.volley.helpers.VolleyNeeds
import tk.jamun.volley.variables.VolleyResponses
import java.util.*
import kotlin.collections.ArrayList

class OrderRepo {
    private var volleyJsonObjectRequest: VolleyJsonObjectRequest? = null
    private var volleyJsonArrayRequest: VolleyJsonArrayRequest? = null

    fun apiGetOrderStatusList(
        modelList: ArrayList<OrderModel>,
        listener: OnEventOccurListener
    ) {
        volleyJsonArrayRequest =
            VolleyJsonArrayRequest(
                URL_GET_ORDER_LIST,
                Array<OrderModel>::class.java,
                object : VolleyResponses() {
                    override fun onResponse(response: Any?, body: String?) {
                        super.onResponse(response, body)
                        modelList.addAll(response as Array<OrderModel>)
                        listener.getEventData(ResponseTypes.TYPE_SUCCESS)
                    }

                    override fun onErrorResponse(statusCode: Int, errorMessage: String?) {
                        super.onErrorResponse(statusCode, errorMessage)
                        Utils.log(statusCode)
                        listener.onErrorResponse(statusCode, errorMessage)
                    }
                })
        VolleyNeeds.get().addCalls(volleyJsonArrayRequest!!)
    }

    fun apiCreateOrder(model: OrderModel, listener: OnEventOccurListener) {
        val jsonObject = JSONObject()
        val jsonObjectAddress = JSONObject()
        val jsonArray = JSONArray()
        for (image in model.images) {
            jsonArray.put(image)
        }
        jsonObjectAddress.put("addressLine", model.address.street)
        jsonObjectAddress.put("city", model.address.city)
        jsonObjectAddress.put("state", model.address.state)
        jsonObjectAddress.put("pincode", model.address.pinCode)

        jsonObject.put("name", model.userName)
        jsonObject.put("email", model.email)
        jsonObject.put("mobileNumber", model.mobileNumber)
        jsonObject.put("address", jsonObjectAddress)
        jsonObject.put("orderImages", jsonArray)
        Utils.log(jsonObject.toString())
        volleyJsonObjectRequest =
            VolleyJsonObjectRequest(
                URL_POST_CREATE_ORDER,
                jsonObject.toString(),
                object : VolleyResponses() {
                    override fun onResponse(response: Any?, body: String?) {
                        super.onResponse(response, body)
                        val jsonObjectResponse = JSONObject(body)
                        if (ParseJson.instance.dataCheck(jsonObjectResponse, "userOrderId")) {
                            model.orderId = jsonObjectResponse.getString("userOrderId")
                            listener.getEventData(ResponseTypes.TYPE_SUCCESS)
                        } else {
                            if (ParseJson.instance.dataCheck(jsonObject, "message")) {
                                listener.onErrorResponse(
                                    ResponseTypes.TYPE_ERROR,
                                    jsonObject.getString("message")
                                )
                            } else {
                                listener.onErrorResponse(
                                    ResponseTypes.TYPE_ERROR,
                                    VolleyErrorExceptions.get().VOLLEY_ERROR_STRING_CHECK_DATA
                                )
                            }
                        }
                    }

                    override fun onErrorResponse(statusCode: Int, errorMessage: String?) {
                        super.onErrorResponse(statusCode, errorMessage)
                        listener.onErrorResponse(statusCode, errorMessage)
                    }
                })
        VolleyNeeds.get().addCalls(volleyJsonObjectRequest!!)
    }

    fun apiGetOrderDetails(id: String, listener: OnEventOccurListener) {
        volleyJsonObjectRequest = VolleyJsonObjectRequest(
                URL_GET_ORDER_DETAILS + id,
                OrderModel::class.java,
                object : VolleyResponses() {
                    override fun onResponse(response: Any?, body: String?) {
                        super.onResponse(response, body)
                        Utils.log(body)
                        listener.getEventData(response as OrderModel)
                    }

                    override fun onErrorResponse(statusCode: Int, errorMessage: String?) {
                        super.onErrorResponse(statusCode, errorMessage)
                        listener.onErrorResponse(statusCode, errorMessage)
                    }
                })
        VolleyNeeds.get().addCalls(volleyJsonObjectRequest!!)
    }

    fun apiCancelOrder(model: OrderModel, listener: OnEventOccurListener) {
        val jsonObject = JSONObject()
        jsonObject.put("userOrderId", model.orderId)
        jsonObject.put("comment", "")
        VolleySolutions.instance.postCommonTasks(
            URL_POST_ORDER_CANCEL,
            jsonObject.toString(),
            listener
        )
    }

    fun apiConfirmOrder(model: OrderModel, listener: OnEventOccurListener) {
        val jsonObject = JSONObject()
        jsonObject.put("userOrderId", model.orderId)
        Utils.log(jsonObject.toString())
        VolleySolutions.instance.postCommonTasks(
            URL_POST_ORDER_CONFIRM,
            jsonObject.toString(),
            listener
        )
    }

    companion object {
        @get:Synchronized
        val instance = OrderRepo()
    }
}
