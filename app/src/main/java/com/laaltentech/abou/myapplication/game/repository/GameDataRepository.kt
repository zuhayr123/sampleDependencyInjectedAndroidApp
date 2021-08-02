package com.laaltentech.abou.myapplication.game.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.laaltentech.abou.myapplication.di.WebService
import com.laaltentech.abou.myapplication.game.data.*
import com.laaltentech.abou.myapplication.network.NetworkBoundResource
import com.laaltentech.abou.myapplication.network.Resource
import com.laaltentech.abou.myapplication.util.ApiResponse
import com.laaltentech.abou.myapplication.util.AppExecutors
import com.laaltentech.abou.myapplication.util.LiveDataCallAdapter
import com.laaltentech.abou.myapplication.util.URL_HUB
import javax.inject.Inject

class GameDataRepository@Inject constructor(
    private val webService: WebService,
    private val appExecutors: AppExecutors,
    private val gameDAO: GameDAO){

//    fun insertGameData(data : GameData, gameId : String) : LiveData<Resource<GameDataWithIndividualRelation>>{
//        return object : NetworkBoundResource<GameDataWithIndividualRelation, GameDataResponse>(appExecutors){
//            override fun saveCallResult(item: GameDataResponse) {
//                if(item.status == "success"){
//                    gameDAO.insertGameData(item.gameData!!)
//                }
//            }
//
////            https://graph.facebook.com/1679081245630678?fields=id,name,email,picture&access_token=EAAS0szaTKZBABAMIMIAFTkOtM6LtuC8GF5IcXRxQhxder9iVIQdeMNycHuQsiUdtwsga11i8X6eSqPQZBwYmOZAZAXHoZBLUmNbTLRC0l9bPXaB3Msj6d9BZC25bCjQGwqievO3F5hXZC7THc0p8N0ZARaIJns0kKBNgII9lbZBZCNv2uiCJxKX7zAV90j97Kz5M2N727ZBUOvW57ylCHzztVCBw40yU8FrWY6KT0gdZBRScDZBmZCICdx4PY0
//
//            override fun shouldFetch(data: GameDataWithIndividualRelation?): Boolean = true
//
//            override fun loadFromDb(): LiveData<GameDataWithIndividualRelation> {
//               return gameDAO.loadAll(gameId = gameId)
//            }
//
//            override fun createCall(): LiveData<ApiResponse<GameDataResponse>> {
//                return webService.insertGameData(gameData = data, url = URL_HUB.INSERT_GAME_DATA)
//            }
//
//            override fun uploadTag(): String? = null
//
//        }.asLiveData()
//    }

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