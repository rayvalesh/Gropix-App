package com.coagere.gropix.jetpack.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ItemModel(
    @SerializedName("itemId")
    var id: String,
    @SerializedName("itemName")
    var name: String,
    @SerializedName("itemImage")
    var image: String,

    @SerializedName("itemPrice")
    var itemPrice: Int = 0,

    @SerializedName("numberOfItem")
    var times: Int = 0,

    @SerializedName("itemsPriceWithQuantity")
    var priceList: Array<PriceModel> = emptyArray(),

    @SerializedName("itemAvailableStates")
    var stateList: Array<String> = emptyArray(),

    @SerializedName("categoryId")
    var categoryId: String? = null,

    @SerializedName("categoryName")
    var categoryName: String
) : Parcelable {
//    @NonParcelField
//    @IgnoredOnParcel
//    @Exclude
//    var adapter: ItemAdapter.ViewHolder? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ItemModel

        if (id != other.id) return false
        if (name != other.name) return false
        if (image != other.image) return false
        if (itemPrice != other.itemPrice) return false
        if (times != other.times) return false
        if (!priceList.contentEquals(other.priceList)) return false
        if (!stateList.contentEquals(other.stateList)) return false
        if (categoryId != other.categoryId) return false
        if (categoryName != other.categoryName) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + image.hashCode()
        result = 31 * result + itemPrice.hashCode()
        result = 31 * result + times
        result = 31 * result + priceList.contentHashCode()
        result = 31 * result + stateList.contentHashCode()
        result = 31 * result + (categoryId?.hashCode() ?: 0)
        result = 31 * result + categoryName.hashCode()
        return result
    }


}