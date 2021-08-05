package com.laaltentech.abou.myapplication.facebook.data

import com.google.gson.annotations.SerializedName

class FacebookProfilePictureData {

    @SerializedName("height")
    var height: Int? = null

    @SerializedName("is_silhouette")
    var silhouette: Boolean? = null

    @SerializedName("url")
    var url: String? = null

    @SerializedName("width")
    var width: Int? = null
}