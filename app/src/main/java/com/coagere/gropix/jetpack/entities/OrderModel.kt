package com.darubaba.android.jetpack.entities

import com.google.gson.annotations.SerializedName

class OrderModel {
    @SerializedName("orderId")
    var orderId: String? = null

    @SerializedName("tax")
    var tax: Int = 0

    @SerializedName("totalAmount")
    var totalAmount: Double = 0.toDouble()
}