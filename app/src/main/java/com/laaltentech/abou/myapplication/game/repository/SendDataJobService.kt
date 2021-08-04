package com.laaltentech.abou.myapplication.game.repository

import android.app.job.JobParameters
import android.app.job.JobService
import android.os.PersistableBundle
import android.util.Log
import com.google.gson.Gson
import com.laaltentech.abou.myapplication.di.Injectable
import com.laaltentech.abou.myapplication.di.WebService
import com.laaltentech.abou.myapplication.game.data.FacebookPageData
import com.laaltentech.abou.myapplication.game.data.FacebookSendDetailsResponse
import com.laaltentech.abou.myapplication.util.AppExecutors
import com.laaltentech.abou.myapplication.util.ServiceBuilder
import com.laaltentech.abou.myapplication.util.URL_HUB
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class SendDataJobService : JobService() {

    override fun onStartJob(params: JobParameters?): Boolean {
        val bundle: PersistableBundle? = params?.extras
        val serializedData = Gson()
        val facebookPageData: FacebookPageData? = serializedData.fromJson(
            bundle?.getString("serializedData"),
            FacebookPageData::class.java
        )

        val webService = ServiceBuilder.buildService(WebService::class.java)
        val myResponse: Call<FacebookSendDetailsResponse> = webService.insertPageDataBG(facebookPageData = facebookPageData!!, url = URL_HUB.POST_DATA_URL)

        myResponse.enqueue(object : Callback<FacebookSendDetailsResponse> {
            override fun onResponse(
                call: Call<FacebookSendDetailsResponse>,
                response: Response<FacebookSendDetailsResponse>
            ) {
                Log.e("Res", "HOLY MOTHER OF GOD ${Gson().toJson(response.body())}")
            }

            override fun onFailure(call: Call<FacebookSendDetailsResponse>, t: Throwable) {
                Log.e("Res", "ATLEAST SOMETHING WORKED")
            }

        })

        Log.e("DATA REACHED", "DATA REACHED THE JOB SERVICE ${Gson().toJson(facebookPageData)}")

        return false
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        TODO("Not yet implemented")
    }
}