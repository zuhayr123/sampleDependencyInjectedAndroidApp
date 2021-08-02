package com.laaltentech.abou.myapplication.game.data

import com.google.gson.annotations.SerializedName

class PictureResponse {
    @SerializedName("data")
    var profileData: FacebookProfilePictureData? = null
}