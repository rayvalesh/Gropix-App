package com.coagere.gropix.jetpack.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.tc.utils.variables.interfaces.ApiKeys
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AddressModel(
    @SerializedName("addressLine")
    var street: String? = null,
    @SerializedName("city")
    var city: String? = null,
    @SerializedName("state")
    var state: String? = null,
    @SerializedName("country")
    var country: String? = null,
    @SerializedName("pincode")
    var pinCode: String? = null,
) : Parcelable
