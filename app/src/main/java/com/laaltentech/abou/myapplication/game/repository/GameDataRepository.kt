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
}