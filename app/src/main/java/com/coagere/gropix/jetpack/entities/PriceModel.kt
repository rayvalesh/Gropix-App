package com.coagere.gropix.jetpack.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PriceModel(
    @SerializedName("id")
    var id: String = "",
    @SerializedName("title")
    var title: String? = "",
    @SerializedName("amount")
    var price: Int = 0,

    var selectedCount: Int = 0
) : Parcelable