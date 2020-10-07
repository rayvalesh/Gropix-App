package com.coagere.gropix.jetpack.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderModel(
    @SerializedName("orderId")
    var orderId: String? = null,

    @SerializedName("name")
    var title: String? = null,

    @SerializedName("timestamp")
    var timestamp: String? = null,

    @SerializedName("image")
    var image: String? = null,

    @SerializedName("tax")
    var tax: Int = 0,

    @SerializedName("status")
    var status: Int = 0,

    @SerializedName("totalAmount")
    var totalAmount: Double = 0.toDouble(),

    @SerializedName("address")
    var address: AddressModel = AddressModel(),

    @SerializedName("fileModels")
    var fileModels: Array<FileModel> = emptyArray(),

) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OrderModel

        if (orderId != other.orderId) return false
        if (title != other.title) return false
        if (timestamp != other.timestamp) return false
        if (image != other.image) return false
        if (tax != other.tax) return false
        if (status != other.status) return false
        if (totalAmount != other.totalAmount) return false
        if (address != other.address) return false
        if (!fileModels.contentEquals(other.fileModels)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = orderId?.hashCode() ?: 0
        result = 31 * result + (title?.hashCode() ?: 0)
        result = 31 * result + (timestamp?.hashCode() ?: 0)
        result = 31 * result + (image?.hashCode() ?: 0)
        result = 31 * result + tax
        result = 31 * result + status
        result = 31 * result + totalAmount.hashCode()
        result = 31 * result + address.hashCode()
        result = 31 * result + fileModels.contentHashCode()
        return result
    }
}