package com.coagere.gropix.jetpack.viewmodels

import androidx.lifecycle.ViewModel
import com.coagere.gropix.jetpack.entities.OrderModel
import com.coagere.gropix.jetpack.repos.OrderRepo
import com.tc.utils.variables.abstracts.OnEventOccurListener

class OrderVM : ViewModel() {

    fun getApiOrderStatusList(
        moduleType: Int,
        modelList: ArrayList<OrderModel>,
        listener: OnEventOccurListener
    ) {
        OrderRepo.instance.apiGetOrderStatusList(moduleType, modelList, listener)
    }

    fun clearEverything() {
    }

}
