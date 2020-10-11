package com.coagere.gropix.jetpack.viewmodels

import androidx.lifecycle.ViewModel
import com.coagere.gropix.jetpack.entities.OrderModel
import com.coagere.gropix.jetpack.repos.OrderRepo
import com.tc.utils.variables.abstracts.OnEventOccurListener

class OrderVM : ViewModel() {

    fun getApiOrderList(
        modelList: ArrayList<OrderModel>,
        listener: OnEventOccurListener
    ) {
        OrderRepo.instance.apiGetOrderStatusList(modelList, listener)
    }


    fun performCreateOrder(model: OrderModel, listener: OnEventOccurListener) {
        OrderRepo.instance.apiCreateOrder(model, listener)
    }

    fun getApiOrderDetails(id : String, listener: OnEventOccurListener) {
        OrderRepo.instance.apiGetOrderDetails(id, listener)
    }

    fun cancelOrder(model: OrderModel, listener: OnEventOccurListener) {
        OrderRepo.instance.apiCancelOrder(model, listener)
    }

    fun confirmOrder(model: OrderModel, listener: OnEventOccurListener) {
        OrderRepo.instance.apiConfirmOrder(model, listener)
    }

}
