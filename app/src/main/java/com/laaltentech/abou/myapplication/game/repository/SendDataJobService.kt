package com.laaltentech.abou.myapplication.game.repository

import android.app.job.JobParameters
import android.app.job.JobService
import android.os.PersistableBundle
import android.util.Log
import com.google.gson.Gson
import com.laaltentech.abou.myapplication.game.data.FacebookPageData


class SendDataJobService : JobService() {
    override fun onStartJob(params: JobParameters?): Boolean {
        val bundle : PersistableBundle? = params?.extras
        val serializedData = Gson()
        val facebookPageData : FacebookPageData? = serializedData.fromJson(bundle?.getString("serializedData"), FacebookPageData::class.java)

        Log.e("DATA REACHED", "DATA REACHED THE JOB SERVICE ${Gson().toJson(facebookPageData)}")

        return false
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        TODO("Not yet implemented")
    }
}