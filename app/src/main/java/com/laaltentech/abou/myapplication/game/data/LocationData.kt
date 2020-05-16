package com.laaltentech.abou.myapplication.game.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "GameData")
class LocationData{
    @PrimaryKey(autoGenerate = false)

    @SerializedName("timestamp")
    var timestamp: String = System.currentTimeMillis().toString()

    @SerializedName("longitude")
    var longitude: String? = "Zuhayr"

    @SerializedName("latitude")
    var latitude: String? = "100"

    @SerializedName("driver_id")
    var driver_id: String? = "test"
}