package com.coagere.gropix.jetpack.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName

import kotlinx.android.parcel.Parcelize
import tk.jamun.volley.variables.Exclude

@Parcelize
data class PlacesModel(
    @SerializedName("placeId")
    var placeId: String = "",
    @SerializedName("primary")
    var primary: String = "",
    @SerializedName("secondary")
    var secondary: String = "",
    @SerializedName("type")
    var type: Int = 0,
    @SerializedName("latitude")
    var latitude: Double = 0.toDouble(),
    @SerializedName("longitude")
    var longitude: Double = 0.toDouble(),
    @SerializedName("image")
    var image: String? = null,

    @SerializedName("rating")
    var rating: Double = 0.toDouble(),
) : Parcelable
