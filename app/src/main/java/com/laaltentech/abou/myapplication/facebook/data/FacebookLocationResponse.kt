package com.laaltentech.abou.myapplication.facebook.data

import com.google.gson.annotations.SerializedName

class FacebookLocationResponse {
    @SerializedName("latitude")
    var latitude: Double = 0.0

    @SerializedName("longitude")
    var longitude: Double = 0.0

    @SerializedName("street")
    var street: String? = null

    @SerializedName("zip")
    var zip: String? = null
}