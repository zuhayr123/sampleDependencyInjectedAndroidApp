package com.laaltentech.abou.myapplication.facebook.data

import com.google.gson.annotations.SerializedName

class PictureResponse {
    @SerializedName("data")
    var profileData: FacebookProfilePictureData? = null
}