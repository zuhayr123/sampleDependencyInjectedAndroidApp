package com.laaltentech.abou.myapplication.facebook.data

import com.google.gson.annotations.SerializedName

class FacebookNumberOfPageResponse {
    @SerializedName("data")
    var data: List<FacebookPageListData>? = null
}