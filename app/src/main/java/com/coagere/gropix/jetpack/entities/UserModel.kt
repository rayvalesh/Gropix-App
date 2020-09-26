package com.coagere.gropix.jetpack.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.tc.utils.variables.interfaces.Constants
import kotlinx.android.parcel.Parcelize

@Parcelize
class UserModel(
        @SerializedName("user_id")
        var userId: String = "",

        @SerializedName("name")
        var name: String? = null,

        @SerializedName("phone")
        var number: String? = null,

        @SerializedName("display_name")
        var displayName: String? = null,

        @SerializedName("user_image")
        var userImage: String? = null,

        @SerializedName("dob")
        var dob: String? = null,

        @SerializedName("gender")
        var gender: Int = Constants.VALUE_MALE,


) : Parcelable
