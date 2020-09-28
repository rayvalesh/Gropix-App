package com.coagere.gropix.jetpack.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderModel(
    @SerializedName("orderId")
    var orderId: String? = null,

    @SerializedName("name")
    var title: String? = null,

    @SerializedName("timestamp")
    var timestamp: String? = null,

    @SerializedName("image")
    var image: String? = null,

    @SerializedName("tax")
    var tax: Int = 0,

    @SerializedName("status")
    var status: Int = 0,

    @SerializedName("totalAmount")
    var totalAmount: Double = 0.toDouble(),

    @SerializedName("address")
    var address: AddressModel = AddressModel()
) : Parcelable