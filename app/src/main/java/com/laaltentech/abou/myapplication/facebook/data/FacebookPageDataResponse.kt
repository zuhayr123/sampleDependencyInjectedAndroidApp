package com.laaltentech.abou.myapplication.facebook.data

import com.google.gson.annotations.SerializedName

class FacebookPageDataResponse {

    @SerializedName("about")
    var about: String? = null

    @SerializedName("access_token")
    var accessToken: String? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("founded")
    var founded: String? = null

    @SerializedName("category")
    var category: String? = null

    @SerializedName("company_overview")
    var companyOverview: String? = null

    @SerializedName("cover")
    var cover: FacebookCoverImageResponse? = null

    @SerializedName("engagement")
    var engagement: FacebookEngagementResponse? = null

    @SerializedName("fan_count")
    var fanCount: Int? = null

    @SerializedName("followers_count")
    var followersCount: Int? = null

    @SerializedName("has_whatsapp_business_number")
    var hasWhatsappBusinessNumber: Boolean? = null

    @SerializedName("has_whatsapp_number")
    var hasWhatsappNumber: Boolean? = null

    @SerializedName("link")
    var link: String? = null

    @SerializedName("username")
    var username: String? = null

    @SerializedName("picture")
    var picture: PictureResponse? = null

    @SerializedName("phone")
    var phone: String? = null

    @SerializedName("description")
    var description: String? = null

    @SerializedName("website")
    var website: String? = null

    @SerializedName("emails")
    var emails: List<String>? = null

    @SerializedName("birthday")
    var birthday: String? = null

    @SerializedName("current_location")
    var currentLocation: String? = null

    @SerializedName("rating_count")
    var ratingCount: Int? = null

    @SerializedName("location")
    var location: FacebookLocationResponse? = null

    @SerializedName("contact_address")
    var contactAddress: FacebookAddressResponse? = null

    @SerializedName("id")
    var id: String? = null

}