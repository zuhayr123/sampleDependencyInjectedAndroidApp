package com.laaltentech.abou.myapplication.facebook.data

import com.google.gson.annotations.SerializedName

class FacebookSendDetailsResponse {
    @SerializedName("result")
    var result: List<FacebookPageData>? = null

    @SerializedName("status")
    var status: String? = null
}