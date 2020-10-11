package com.coagere.gropix.jetpack.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.tc.utils.variables.interfaces.Constants
import kotlinx.android.parcel.Parcelize

@Parcelize
class UserModel(
        @SerializedName("name")
        var name: String? = null,

) : Parcelable
