package com.laaltentech.abou.myapplication.di

import androidx.lifecycle.LiveData
import com.laaltentech.abou.myapplication.game.data.*
import com.laaltentech.abou.myapplication.util.ApiResponse
import retrofit2.http.*

interface WebService {
    //todo just an example to use in future cases

    @POST
    fun insertGameData(@Url url: String,
                          @Body gameData : GameData) : LiveData<ApiResponse<GameDataResponse>>

    @GET
    fun fetchProfileData(@Url url: String,
                         @Query("fields") fields : String,
                         @Query("access_token") accessToken : String) : LiveData<ApiResponse<FacebookProfileResponse>>

    @GET
    fun fetchPageList(@Url url: String,
                      @Query("access_token") accessToken : String) : LiveData<ApiResponse<FacebookNumberOfPageResponse>>

    @GET
    fun fetchPageData(@Url url: String,
                      @Query("fields") fields : String,
                      @Query("access_token") accessToken : String) : LiveData<ApiResponse<FacebookPageDataResponse>>
}