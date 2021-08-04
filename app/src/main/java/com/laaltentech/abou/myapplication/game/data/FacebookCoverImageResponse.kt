package com.laaltentech.abou.myapplication.game.data

import com.google.gson.annotations.SerializedName

class FacebookCoverImageResponse {
    @SerializedName("cover_id")
    var coverId: String = "null"

    @SerializedName("source")
    var source: String? = null

    @SerializedName("id")
    var id: String? = null
}