package com.coagere.gropix.jetpack.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ContactModel(
        @SerializedName("name")
        var name: String? = null,
        @SerializedName("email")
        var email: String? = null,
        @SerializedName("message")
        var message: String? = null
) : Parcelable
