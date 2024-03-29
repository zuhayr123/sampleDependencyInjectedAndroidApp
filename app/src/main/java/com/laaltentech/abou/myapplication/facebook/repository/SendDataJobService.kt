package com.laaltentech.abou.myapplication.facebook.repository

import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.job.JobParameters
import android.app.job.JobService
import android.graphics.Color
import android.os.Build
import android.os.PersistableBundle
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.gson.Gson
import com.laaltentech.abou.myapplication.di.WebService
import com.laaltentech.abou.myapplication.facebook.data.FacebookPageData
import com.laaltentech.abou.myapplication.facebook.data.FacebookSendDetailsResponse
import com.laaltentech.abou.myapplication.util.ServiceBuilder
import com.laaltentech.abou.myapplication.util.URL_HUB
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SendDataJobService : JobService() {

    var mNotifyManager: NotificationManager? = null
    private val PRIMARY_CHANNEL_ID = "primary_notification_channel"

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

                createNotificationChannel()

                var builder = NotificationCompat.Builder(applicationContext, PRIMARY_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_notification_clear_all)
                    .setContentTitle("Notify Data Upload")
                    .setContentText("Your data was successfully uploaded")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setAutoCancel(true)

                mNotifyManager!!.notify(0, builder.build())
//                Log.e("Res", "HOLY MOTHER OF GOD ${Gson().toJson(response.body())}")
            }

            override fun onFailure(call: Call<FacebookSendDetailsResponse>, t: Throwable) {
                Log.e("Res", "ATLEAST SOMETHING WORKED")
            }

        })

//        Log.e("DATA REACHED", "DATA REACHED THE JOB SERVICE ${Gson().toJson(facebookPageData)}")

        return false
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        return false;
    }

    fun createNotificationChannel() {

        // Create a notification manager object.
        mNotifyManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // Notification channels are only available in OREO and higher.
        // So, add a check on SDK version.
        if (Build.VERSION.SDK_INT >=
            Build.VERSION_CODES.O
        ) {
            // Create the NotificationChannel with all the parameters.
            val notificationChannel = NotificationChannel(
                PRIMARY_CHANNEL_ID,
                "Notification Panel",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Please wait while the app is trying to send data to servers."
            mNotifyManager!!.createNotificationChannel(notificationChannel)
        }
    }
}