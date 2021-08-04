package com.laaltentech.abou.myapplication.game.data

import com.google.gson.annotations.SerializedName

class FacebookAddressResponse {
    @SerializedName("id")
    var id: String? = null

    @SerializedName("city")
    var city: String? = null

    @SerializedName("country")
    var country: String? = null

    @SerializedName("postal_code")
    var postalCode: String? = null

    @SerializedName("region")
    var region: String? = null

    @SerializedName("street1")
    var street1: String? = null

    @SerializedName("street2")
    var street2: String? = null
}