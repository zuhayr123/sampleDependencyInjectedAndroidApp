package com.laaltentech.abou.myapplication.util

import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.ServiceConnection
import android.location.Location
import android.os.Binder
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices

class LocationService : Service(),
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
    LocationListener {

    private val binder = LocalBinder()

    inner class LocalBinder : Binder() {
        // Return this instance of LocalService so clients can call public methods
        fun getService(): LocationService = this@LocationService
    }

    override fun onBind(intent: Intent): IBinder {
        mGoogleApiClient?.connect()
        return binder
    }

    override fun onConnected(p0: Bundle?) {
        try {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient)
            if (mLastLocation != null)
                PrefManager.setLocation(mLastLocation?.latitude.toString(), mLastLocation?.longitude.toString())
            LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient,
                mLocationRequest,
                this
            )
            Log.e("Location", "is ${mLastLocation?.longitude}")
        } catch (e: Exception) {
            e.printStackTrace()
            val location = Location("dummy_provider")
            location.latitude = 28.507235
            location.longitude = 77.083144
            onLocationChanged(location)
        }
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onConnectionFailed(p0: ConnectionResult) {

        if (p0.hasResolution()) {
            try {
                p0.startResolutionForResult(
                    applicationContext as Activity,
                    CONNECTION_FAILURE_RESOLUTION_REQUEST
                )
            } catch (e: IntentSender.SendIntentException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            when {
                p0.errorCode == 1 -> {
                    val location = Location("locationProvider")
                    location.latitude = 22.574000
                    location.longitude = 75.744300
                    onLocationChanged(location)
                }
            }
        }
    }

    override fun onLocationChanged(p0: Location?) {
        mLastLocation = p0
        if (p0 != null) {
            PrefManager.setLocation(p0.latitude.toString(), p0.longitude.toString())
        }
    }

    override fun onCreate() {
        super.onCreate()
        PrefManager(applicationContext)
        latLonTask(applicationContext)
    }

    override fun unbindService(conn: ServiceConnection) {
        super.unbindService(conn)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun latLonTask(activity: Context) {

        mGoogleApiClient = GoogleApiClient.Builder(activity)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval((10 * 1000).toLong())        // 10 seconds, in milliseconds
            .setFastestInterval(1000) // 1 second, in milliseconds
    }

    companion object {
        private var mLastLocation: Location? = null
        private var mGoogleApiClient: GoogleApiClient? = null
        private var mLocationRequest: LocationRequest? = null
        private const val CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000
    }

}