package com.coagere.gropix.jetpack.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ItemModel(
    @SerializedName("itemId")
    var itemId: String,

    @SerializedName("itemName")
    var itemName: String,

    @SerializedName("itemPrice")
    var itemPrice: String,

    @SerializedName("itemQuantity")
    var itemQuantity: String,


    @SerializedName("amount")
    var amount: String,

    @SerializedName("status")
    var status: Int = 0,

    @SerializedName("timeStamp")
    var timeStamp: String? = null

) : Parcelable