package com.coagere.gropix.jetpack.repos

import com.coagere.gropix.jetpack.entities.OrderModel
import com.tc.utils.variables.abstracts.OnEventOccurListener
import com.tc.utils.variables.enums.ResponseTypes
import com.tc.utils.variables.interfaces.ApiKeys.Companion.URL_GET_ORDER_DETAILS
import com.tc.utils.variables.interfaces.ApiKeys.Companion.URL_GET_ORDER_LIST
import com.tc.utils.variables.interfaces.ApiKeys.Companion.URL_POST_CREATE_ORDER
import org.json.JSONArray
import org.json.JSONObject
import tk.jamun.ui.snacks.L
import tk.jamun.volley.classes.VolleyJsonArrayRequest
import tk.jamun.volley.classes.VolleyJsonObjectRequest
import tk.jamun.volley.helpers.VolleyNeeds
import tk.jamun.volley.variables.VolleyResponses

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
                        modelList.addAll((response as Array<*>).filterIsInstance<OrderModel>())
                        listener.getEventData(ResponseTypes.TYPE_SUCCESS)
                    }

                    override fun onErrorResponse(statusCode: Int, errorMessage: String?) {
                        super.onErrorResponse(statusCode, errorMessage)
                        listener.onErrorResponse(statusCode, errorMessage)
                    }
                })
        VolleyNeeds.get().addCalls(volleyJsonArrayRequest!!)
    }

    fun apiCreateOrder(model: OrderModel, listener: OnEventOccurListener) {
        val jsonObject = JSONObject()
        val jsonObjectAddress = JSONObject()
        val jsonArray = JSONArray()
        for (image in model.images!!) {
            jsonArray.put(image)
        }
        jsonObjectAddress.put("addressLine", model.address.street)
        jsonObjectAddress.put("city", model.address.city)
        jsonObjectAddress.put("state", model.address.state)
        jsonObjectAddress.put("pincode", model.address.pinCode)

        jsonObject.put("name", model.userName)
        jsonObject.put("email", model.userName)
        jsonObject.put("mobileNumber", model.userName)
        jsonObject.put("address", jsonObjectAddress)
        jsonObject.put("orderImages", jsonArray)


        volleyJsonObjectRequest =
            VolleyJsonObjectRequest(
                URL_POST_CREATE_ORDER,
                jsonObject.toString(),
                object : VolleyResponses() {
                    override fun onResponse(response: Any?, body: String?) {
                        super.onResponse(response, body)
                        listener.getEventData(ResponseTypes.TYPE_SUCCESS)
                    }

                    override fun onErrorResponse(statusCode: Int, errorMessage: String?) {
                        super.onErrorResponse(statusCode, errorMessage)
                        listener.onErrorResponse(statusCode, errorMessage)
                    }
                })
        VolleyNeeds.get().addCalls(volleyJsonObjectRequest!!)
    }

    fun apiGetOrderDetails(model: OrderModel, listener: OnEventOccurListener) {
        volleyJsonObjectRequest =
            VolleyJsonObjectRequest(
                URL_GET_ORDER_DETAILS + model.orderId,
                OrderModel::class.java,
                object : VolleyResponses() {
                    override fun onResponse(response: Any?, body: String?) {
                        super.onResponse(response, body)
                        listener.getEventData(response as OrderModel)
                    }

                    override fun onErrorResponse(statusCode: Int, errorMessage: String?) {
                        super.onErrorResponse(statusCode, errorMessage)
                        listener.onErrorResponse(statusCode, errorMessage)
                    }
                })
        VolleyNeeds.get().addCalls(volleyJsonObjectRequest!!)
    }


    companion object {
        @get:Synchronized
        val instance = OrderRepo()
    }
}
