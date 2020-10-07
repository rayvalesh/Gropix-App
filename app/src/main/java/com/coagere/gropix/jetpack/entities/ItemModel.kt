package com.coagere.gropix.jetpack.entities

import android.os.Parcelable
import androidx.versionedparcelable.NonParcelField
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import tk.jamun.volley.variables.Exclude

@Parcelize
data class ItemModel(
    @SerializedName("itemName")
    var name: String,

    @SerializedName("itemPrice")
    var itemPrice: Int = 0,

    @SerializedName("numberOfItem")
    var times: Int = 0,
) : Parcelable