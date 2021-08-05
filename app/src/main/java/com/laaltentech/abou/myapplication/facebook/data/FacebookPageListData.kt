package com.laaltentech.abou.myapplication.facebook.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "FacebookPageListData")
class FacebookPageListData {
    @PrimaryKey
    @SerializedName("id")
    var id: String = "null"

    @SerializedName("access_token")
    var accessToken: String? = null

    @SerializedName("category")
    var category: String? = null

    @SerializedName("name")
    var name: String? = null


}