package com.coagere.gropix.jetpack.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import tk.jamun.volley.variables.Exclude

@Parcelize
data class OrderModel(
    @SerializedName("userOrderId")
    var orderId: String? = null,

    @SerializedName("userName")
    var userName: String? = null,

    @SerializedName("email")
    var email: String? = null,

    @SerializedName("mobileNumber")
    var mobileNumber: String? = null,

    @SerializedName("timestamp")
    var timestamp: String? = null,

    @SerializedName("updateTimeStamp")
    var updateTimeStamp: String? = null,

    @SerializedName("orderImages")
    var images: ArrayList<String>? = arrayListOf(),

    @SerializedName("status")
    var status: Int = 0,

    @SerializedName("amount")
    var amount: String = "",

    @SerializedName("deliveryFee")
    var deliveryFee: String = "",

    @SerializedName("totalAmount")
    var totalAmount: String = "",

    @SerializedName("discount")
    var discount: String = "",

    @SerializedName("address")
    var address: AddressModel = AddressModel(),

    @SerializedName("itemList")
    var itemList: Array<ItemModel> = emptyArray(),

    @Exclude
    var fileModels: Array<FileModel> = emptyArray(),

    ) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OrderModel

        if (orderId != other.orderId) return false
        if (userName != other.userName) return false
        if (email != other.email) return false
        if (mobileNumber != other.mobileNumber) return false
        if (timestamp != other.timestamp) return false
        if (images != other.images) return false
        if (status != other.status) return false
        if (totalAmount != other.totalAmount) return false
        if (address != other.address) return false
        if (!fileModels.contentEquals(other.fileModels)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = orderId?.hashCode() ?: 0
        result = 31 * result + (userName?.hashCode() ?: 0)
        result = 31 * result + (email?.hashCode() ?: 0)
        result = 31 * result + (mobileNumber?.hashCode() ?: 0)
        result = 31 * result + (timestamp?.hashCode() ?: 0)
        result = 31 * result + (images?.hashCode() ?: 0)
        result = 31 * result + status
        result = 31 * result + totalAmount.hashCode()
        result = 31 * result + address.hashCode()
        result = 31 * result + fileModels.contentHashCode()
        return result
    }


}