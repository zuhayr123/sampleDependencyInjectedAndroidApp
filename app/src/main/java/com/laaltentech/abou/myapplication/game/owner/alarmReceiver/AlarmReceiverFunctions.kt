package com.laaltentech.abou.myapplication.game.owner.alarmReceiver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import okhttp3.*
import java.io.IOException


class AlarmReceiverFunctions : BroadcastReceiver(), LocationListener {

    lateinit var location : Location

    override fun onLocationChanged(p0: Location?) {
        if(p0 != null) {
            location = p0
            Log.e("TAG", "location is lat : ${p0.latitude} and lon is : ${p0.longitude}")
        }
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

    @SuppressLint("MissingPermission")
    override fun onReceive(p0: Context?, p1: Intent?) {

        p1.also {
            val locationManager: LocationManager = p0?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, this)

            val location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            if(location != null){
                this.location = location
            }
            else{
                this.location.latitude = 0.0000
                this.location.longitude = 0.0000
            }

            Log.e("TAG", "lat is ${location?.latitude} and longitude is : ${location?.longitude}")
        }
        Log.e("ALARM WAS FIRED", "THIS ALARM WAS FIRED")
        testPost()
    }

    fun testPost(){
        val okHttpClient = OkHttpClient()
        val requestBody = FormBody.Builder()
            .add("driver_id", "test ID")
            .add("latitude", location.latitude.toString())
            .add("longitude", location.longitude.toString())
            .add("timestamp", System.currentTimeMillis().toString())
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
                Log.e("success", "the details were successfully posted")
                // Handle this
            }
        })
    }
}