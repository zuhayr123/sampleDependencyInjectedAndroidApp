package com.laaltentech.abou.myapplication.game.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "FacebookProfileData")
class FacebookProfileData {
    @PrimaryKey
    @SerializedName("id")
    var id: String = "null"

    @SerializedName("name")
    var name: String? = null

    @SerializedName("email")
    var email: String? = null

    @SerializedName("birthday")
    var birthday: String? = null

    @SerializedName("gender")
    var gender: String? = null

    @SerializedName("height")
    var height: Int? = null

    @SerializedName("is_silhouette")
    var silhouette: Boolean? = null

    @SerializedName("url")
    var url: String? = null

    @SerializedName("width")
    var width: Int? = null


}