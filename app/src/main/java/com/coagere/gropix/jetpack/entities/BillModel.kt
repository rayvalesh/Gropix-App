package com.coagere.gropix.jetpack.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BillModel(
    @SerializedName("billImagePath")
    var billImagePath: String? = null,

    @SerializedName("timeStamp")
    var timeStamp: String? = null

) : Parcelable