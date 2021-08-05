package com.laaltentech.abou.myapplication.game.repository

import androidx.lifecycle.LiveData
import com.facebook.FacebookAuthorizationException
import com.laaltentech.abou.myapplication.di.WebService
import com.laaltentech.abou.myapplication.game.data.*
import com.laaltentech.abou.myapplication.network.NetworkBoundResource
import com.laaltentech.abou.myapplication.network.Resource
import com.laaltentech.abou.myapplication.util.ApiResponse
import com.laaltentech.abou.myapplication.util.AppExecutors
import com.laaltentech.abou.myapplication.util.URL_HUB
import javax.inject.Inject

class GameDataRepository@Inject constructor(
    private val webService: WebService,
    private val appExecutors: AppExecutors,
    private val gameDAO: GameDAO){

    fun deleteFacebookPageData() {
        appExecutors.diskIO().execute {
            gameDAO.deleteFacebookPageData()
        }
    }

    fun fetchProfileData(accessToken: String, isInternet: Boolean, userID: String): LiveData<Resource<FacebookProfileData>>{
        return object : NetworkBoundResource<FacebookProfileData, FacebookProfileResponse>(appExecutors){
            override fun saveCallResult(item: FacebookProfileResponse) {
                gameDAO.insertFacebookProfileData(mapProfileData(item))
            }

            override fun shouldFetch(data: FacebookProfileData?): Boolean  = isInternet

            override fun loadFromDb(): LiveData<FacebookProfileData> {
                return gameDAO.fetchFacebookProfile(userId = userID)
            }

            override fun createCall(): LiveData<ApiResponse<FacebookProfileResponse>> {
                return webService.fetchProfileData(url = URL_HUB.BASE_URL+ userID, fields = "id,name,email,picture,birthday,gender", accessToken = accessToken)
            }

            override fun uploadTag(): String?  = null

        }.asLiveData()
    }

    fun fetchPageList(accessToken: String, isInternet: Boolean, userID: String): LiveData<Resource<List<FacebookPageListData>>> {
        return object : NetworkBoundResource<List<FacebookPageListData>,FacebookNumberOfPageResponse>(appExecutors){
            override fun saveCallResult(item: FacebookNumberOfPageResponse) {
                gameDAO.saveAllFacebookPageListData(userListData = item.data)
            }

            override fun shouldFetch(data: List<FacebookPageListData>?): Boolean  = isInternet

            override fun loadFromDb(): LiveData<List<FacebookPageListData>> {
                return gameDAO.fetchAllFacebookPageListData()
            }

            override fun createCall(): LiveData<ApiResponse<FacebookNumberOfPageResponse>> {
                return webService.fetchPageList(url = URL_HUB.BASE_URL + userID + "/accounts",accessToken = accessToken)
            }

            override fun uploadTag(): String?  = null

        }.asLiveData()
    }

    fun fetchPageData(accessToken: String, isInternet: Boolean, pageID: String): LiveData<Resource<FacebookPageData>>{
        return object : NetworkBoundResource<FacebookPageData, FacebookPageDataResponse>(appExecutors){
            override fun saveCallResult(item: FacebookPageDataResponse) {
                gameDAO.insertFacebookPageData(mapFacebookPageData(item = item))
            }

            override fun shouldFetch(data: FacebookPageData?): Boolean = isInternet

            override fun loadFromDb(): LiveData<FacebookPageData> {
                return gameDAO.fetchAllFacebookPageData(pageID = pageID)
            }

            override fun createCall(): LiveData<ApiResponse<FacebookPageDataResponse>> {
                return webService.fetchPageData(url = URL_HUB.BASE_URL + pageID,accessToken = accessToken, fields = "about,attire,access_token,bio,location,parking,hours,emails, name, category, birthday,company_overview,contact_address,cover,current_location,description,engagement,fan_count,followers_count,founded,has_whatsapp_business_number,has_whatsapp_number,link,phone,rating_count,username,website,picture")
            }

            override fun uploadTag(): String? = null

        }.asLiveData()
    }

    fun postPageData(facebookPageData: FacebookPageData, isInternet: Boolean, pageID: String): LiveData<Resource<FacebookPageData>>{
        return object : NetworkBoundResource<FacebookPageData, FacebookSendDetailsResponse>(appExecutors){
            override fun saveCallResult(item: FacebookSendDetailsResponse) {
                when(item.status){
                    "success" ->{
                        //DO something if needed when the query is successful.
                    }
                }
            }

            override fun shouldFetch(data: FacebookPageData?): Boolean = isInternet

            override fun loadFromDb(): LiveData<FacebookPageData> {
                return gameDAO.fetchAllFacebookPageData(pageID = pageID)
            }

            override fun createCall(): LiveData<ApiResponse<FacebookSendDetailsResponse>> {
                return webService.insertPageData(url = URL_HUB.POST_DATA_URL, facebookPageData = facebookPageData)
            }

            override fun uploadTag(): String? = null

        }.asLiveData()
    }

    fun mapProfileData(item: FacebookProfileResponse): FacebookProfileData{
        val data = FacebookProfileData()
        data.id = item.id!!
        data.email = item.email
        data.name = item.name
        data.gender = item.gender
        data.birthday = item.birthday
        data.height = item.picture?.profileData?.height
        data.silhouette = item.picture?.profileData?.silhouette
        data.url = item.picture?.profileData?.url
        data.width = item.picture?.profileData?.width
        return data
    }

    fun mapFacebookPageData(item: FacebookPageDataResponse) : FacebookPageData{
        val data = FacebookPageData()

        data.about = item.about
        data.accessToken = item.accessToken
        data.birthday = item.birthday
        data.category = item.category
        data.company_overview = item.companyOverview
        data.engagement = item.engagement?.count.toString()
        data.pageId = item.id!!
        data.name = item.name

        item.contactAddress.let {
            var address = ""

            if(it?.street1 != null){
                address += it.street1
            }

            if(it?.street2 != null){
                address += ", " + it.street2
            }

            if(it?.city != null){
                address += ", " + it.city
            }

            if(it?.postalCode != null){
                address += ", " + it.postalCode
            }

            if(it?.country != null){
                address += ", " + it.country
            }

            data.contact_address = address
        }
        data.cover = item.cover?.source
        data.current_location = item.currentLocation
        data.description = item.description
        data.emails = item.emails.toString()
        data.fan_count = item.fanCount.toString()
        data.followers_count = item.followersCount.toString()
        data.has_whatsapp_business_number = item.hasWhatsappBusinessNumber.toString()
        data.has_whatsapp_number = item.hasWhatsappNumber.toString()
        data.link = item.link
        data.location = item.location?.street + ", " +item.location?.zip
        data.phone = item.phone
        data.rating_count = item.ratingCount.toString()
        data.username = item.username
        data.website = item.website
        data.picture = item.picture?.profileData?.url
        data.founded = item.founded


        return data
    }
}