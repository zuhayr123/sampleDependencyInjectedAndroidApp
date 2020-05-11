package com.laaltentech.abou.myapplication.game.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "GameData")
class GameData{
    @PrimaryKey(autoGenerate = false)

    @SerializedName("game_id")
    var game_id: String = System.currentTimeMillis().toString()

    @SerializedName("name")
    var name: String? = "Zuhayr"

    @SerializedName("score")
    var score: String? = null

    @SerializedName("game_details")
    var individualScores: List<IndividualGameScore>? = null
}