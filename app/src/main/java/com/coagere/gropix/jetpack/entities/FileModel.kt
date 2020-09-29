package com.coagere.gropix.jetpack.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.tc.utils.variables.interfaces.ApiKeys
import kotlinx.android.parcel.Parcelize
import tk.jamun.volley.variables.Exclude

/**
 * [FileModel] have all information related fields to any media file, also uploading progress states and values.
 */
@Parcelize
data class FileModel(
        @SerializedName("fileUrl")
        var fileUrl: String? = null,

        @SerializedName("url")
        var downloadUrl: String? = null,

        @SerializedName("fileName")
        var fileName: String? = null,

        @SerializedName("displaySize")
        var displaySize: String? = null,

        @SerializedName("fileSize")
        var fileSize: Long = 0,

        @SerializedName("extension")
        var fileType: String? = null,
        @Exclude
        var actionType: Int = 0,
        @Exclude
        var progress: Int = 0,
        @Exclude
        var progressData: String? = null


) : Parcelable {

}