package com.laaltentech.abou.myapplication.game.data

import com.google.gson.annotations.SerializedName

class FacebookEngagementResponse {
    @SerializedName("count")
    var count: Int = 0

    @SerializedName("social_sentence")
    var socialSentence: String? = null
}