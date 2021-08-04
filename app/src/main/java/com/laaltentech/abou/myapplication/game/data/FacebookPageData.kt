package com.laaltentech.abou.myapplication.game.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "FacebookPageData")
class FacebookPageData {
    @PrimaryKey
    @SerializedName("page_id")
    var pageId: String = "null"

    @SerializedName("name")
    var name: String = "null"

    @SerializedName("access_token")
    var accessToken: String = "null"

    @SerializedName("category")
    var category: String = "null"

    @SerializedName("about")
    var about: String = "null"

    @SerializedName("birthday")
    var birthday: String = "null"

    @SerializedName("business")
    var business: String = "null"

    @SerializedName("company_overview")
    var company_overview: String = "null"

    @SerializedName("contact_address")
    var contact_address: String = "null"

    @SerializedName("cover")
    var cover: String = "null"

    @SerializedName("current_location")
    var current_location: String = "null"

    @SerializedName("description")
    var description: String = "null"

    @SerializedName("emails")
    var emails: String = "null"

    @SerializedName("engagement")
    var engagement: String = "null"

    @SerializedName("fan_count")
    var fan_count: String = "null"

    @SerializedName("followers_count")
    var followers_count: String = "null"

    @SerializedName("has_whatsapp_business_number")
    var has_whatsapp_business_number: String = "null"

    @SerializedName("has_whatsapp_number")
    var has_whatsapp_number: String = "null"

    @SerializedName("link")
    var link: String = "null"

    @SerializedName("location")
    var location: String = "null"

    @SerializedName("rating_count")
    var rating_count: String = "null"

    @SerializedName("username")
    var username: String = "null"

    @SerializedName("website")
    var website: String = "null"

    @SerializedName("picture")
    var picture: String = "null"


}