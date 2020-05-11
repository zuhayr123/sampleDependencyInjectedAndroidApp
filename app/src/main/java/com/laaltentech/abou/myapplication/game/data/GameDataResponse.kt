package com.laaltentech.abou.myapplication.game.data

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

class GameDataResponse{
    @PrimaryKey(autoGenerate = false)

    @SerializedName("msg")
    var game_id: String? = null

    @SerializedName("game_data")
    var gameData: GameData? = null

    @SerializedName("status")
    var status: String? = null
}