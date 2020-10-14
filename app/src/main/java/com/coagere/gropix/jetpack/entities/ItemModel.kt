package com.coagere.gropix.jetpack.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ItemModel(
    @SerializedName("itemName")
    var itemName: String? = null,

    @SerializedName("itemPrice")
    var itemPrice: String? = null,

    @SerializedName("itemQuantity")
    var itemQuantity: String? = null,

    @SerializedName("amount")
    var amount: String? = null,
) : Parcelable