package com.laaltentech.abou.myapplication.game.repository

import androidx.lifecycle.LiveData
import com.laaltentech.abou.myapplication.di.WebService
import com.laaltentech.abou.myapplication.game.data.FacebookProfileData
import com.laaltentech.abou.myapplication.game.data.FacebookProfileResponse
import com.laaltentech.abou.myapplication.game.data.GameDAO
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
                return webService.fetchProfileData(url = URL_HUB.BASE_URL+ userID, fields = "id,name,email,picture", accessToken = accessToken)
            }

            override fun uploadTag(): String?  = null

        }.asLiveData()
    }

    fun mapProfileData(item: FacebookProfileResponse): FacebookProfileData{
        val data = FacebookProfileData()
        data.id = item.id!!
        data.email = item.email
        data.name = item.name
        data.height = item.picture?.profileData?.height
        data.silhouette = item.picture?.profileData?.silhouette
        data.url = item.picture?.profileData?.url
        data.width = item.picture?.profileData?.width
        return data
    }
}