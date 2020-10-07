package com.coagere.gropix.jetpack.repos

import com.coagere.gropix.jetpack.entities.ItemModel
import com.coagere.gropix.jetpack.entities.OrderModel
import com.tc.utils.variables.abstracts.OnEventOccurListener
import com.tc.utils.variables.enums.ResponseTypes
import com.tc.utils.variables.interfaces.ApiKeys.Companion.URL_GET_ORDER_ITEM_LIST
import com.tc.utils.variables.interfaces.ApiKeys.Companion.URL_GET_ORDER_LIST
import com.tc.utils.variables.interfaces.Constants
import tk.jamun.volley.classes.VolleyJsonObjectRequest
import tk.jamun.volley.helpers.VolleyNeeds
import tk.jamun.volley.variables.VolleyResponses

class OrderRepo {
    private var volleyJsonObjectRequest: VolleyJsonObjectRequest? = null

    fun apiGetOrderStatusList(
        moduleType: Int,
        modelList: ArrayList<OrderModel>,
        listener: OnEventOccurListener
    ) {
        var url = URL_GET_ORDER_LIST
        url += when (moduleType) {
            Constants.MODULE_PENDING -> {
                "pending"
            }
            Constants.MODULE_PLACED -> {
                "placed"
            }
            else -> {
                "cancelled"
            }
        }
        volleyJsonObjectRequest =
            VolleyJsonObjectRequest(
                url,
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
        VolleyNeeds.get().addCalls(volleyJsonObjectRequest!!)
    }

    fun apiGetOrderItemList(model: OrderModel, listener: OnEventOccurListener) {
        volleyJsonObjectRequest =
            VolleyJsonObjectRequest(
                URL_GET_ORDER_ITEM_LIST + model.orderId,
                Array<OrderModel>::class.java,
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


    companion object {
        @get:Synchronized
        val instance = OrderRepo()
    }
}
