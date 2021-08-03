package com.laaltentech.abou.myapplication.game.data

import com.google.gson.annotations.SerializedName

class FacebookProfileResponse {
    @SerializedName("id")
    var id: String? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("email")
    var email: String? = null

    @SerializedName("gender")
    var gender: String? = null

    @SerializedName("birthday")
    var birthday: String? = null

    @SerializedName("picture")
    var picture: PictureResponse? = null
}
