package com.laaltentech.abou.myapplication.facebook.repository
import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.laaltentech.abou.myapplication.di.WebService
import com.laaltentech.abou.myapplication.facebook.data.FacebookPageData
import com.laaltentech.abou.myapplication.facebook.data.FacebookSendDetailsResponse
import com.laaltentech.abou.myapplication.util.ServiceBuilder
import com.laaltentech.abou.myapplication.util.URL_HUB
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SendDataWorkManager(context : Context, params : WorkerParameters) : Worker(context, params) {

    var mNotifyManager: NotificationManager? = null
    private val PRIMARY_CHANNEL_ID = "primary_notification_channel"

    override fun doWork(): Result {
        val serializedData = Gson()
        val facebookPageData: FacebookPageData? = serializedData.fromJson(
            inputData.getString("serializedData"),
            FacebookPageData::class.java
        )

//        Log.e("DATA REACHED", "DATA REACHED THE WORK MANAGER ${Gson().toJson(facebookPageData)}")


        val webService = ServiceBuilder.buildService(WebService::class.java)
        val myResponse: Call<FacebookSendDetailsResponse> = webService.insertPageDataBG(facebookPageData = facebookPageData, url = URL_HUB.POST_DATA_URL)

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
            }

            override fun onFailure(call: Call<FacebookSendDetailsResponse>, t: Throwable) {
            }

        })

        return Result.success();
    }

    fun createNotificationChannel() {
        // Create a notification manager object.
        mNotifyManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

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