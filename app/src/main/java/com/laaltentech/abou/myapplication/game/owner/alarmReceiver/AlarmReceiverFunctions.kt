package com.laaltentech.abou.myapplication.game.owner.alarmReceiver

import android.annotation.SuppressLint
import android.content.*
import android.location.Location
import android.location.LocationListener
import android.media.MediaPlayer
import android.provider.Settings
import android.util.Log
import okhttp3.*
import java.io.IOException
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import androidx.core.content.ContextCompat.*
import com.laaltentech.abou.myapplication.util.LocationService
import android.R.attr.name




class AlarmReceiverFunctions : BroadcastReceiver(), LocationListener {
    override fun onLocationChanged(p0: Location?) {
        Log.e("TAG", "location is lat : ${p0?.latitude } and lon is : ${p0?.longitude}")
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
        Log.e("TAG", "location status changed")
    }

    override fun onProviderEnabled(p0: String?) {
        Log.e("TAG", "location is enabled")
    }

    override fun onProviderDisabled(p0: String?) {
        Log.e("TAG", "location is disabled")

    }

    private lateinit var mService: LocationService

    private val connection = object : ServiceConnection {
        override fun onServiceDisconnected(p0: ComponentName?) {
        }

        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            val binder = p1 as LocationService.LocalBinder
            mService = binder.getService()
        }
    }

    @SuppressLint("MissingPermission")
    override fun onReceive(p0: Context?, p1: Intent?) {

        p1.also { intent ->
//            p0?.bindService(intent, connection, Context.BIND_AUTO_CREATE)
            val locationManager: LocationManager = p0?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, this)
            val location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            Log.e("TAG", "lat is ${location?.latitude} and longitude is : ${location?.longitude}")
        }

//        val mediaPlayer : MediaPlayer = MediaPlayer.create(p0, Settings.System.DEFAULT_RINGTONE_URI)
//        mediaPlayer.start()


        Log.e("ALARM WAS FIRED", "THIS ALARM WAS FIRED")
        testPost()
    }

    fun testPost(){
        val okHttpClient = OkHttpClient()
        val requestBody = FormBody.Builder()
            .add("driver_id", "some_email")
            .add("latitude", "82")
            .add("longitude", "723")
            .add("timestamp", "some_password")
            .build()

        val request = Request.Builder()
            .method("POST", requestBody)
            .url("https://fleetxs.herokuapp.com/api/location_gps")
            .build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("faliure", "failed to push")
                // Handle this
            }

            override fun onResponse(call: Call, response: Response) {
                Log.e("success", "failed to push")
                // Handle this
            }
        })
    }
}