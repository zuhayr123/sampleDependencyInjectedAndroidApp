package com.laaltentech.abou.myapplication.di

import androidx.lifecycle.LiveData
import com.laaltentech.abou.myapplication.facebook.data.*
import com.laaltentech.abou.myapplication.util.ApiResponse
import retrofit2.Call
import retrofit2.http.*

interface WebService {
    //todo just an example to use in future cases

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

    @POST
    fun insertPageData(@Url url: String,
                       @Body facebookPageData : FacebookPageData?) : LiveData<ApiResponse<FacebookSendDetailsResponse>>

    @POST
    fun insertPageDataBG(@Url url: String,
                       @Body facebookPageData : FacebookPageData?) : Call<FacebookSendDetailsResponse>
}