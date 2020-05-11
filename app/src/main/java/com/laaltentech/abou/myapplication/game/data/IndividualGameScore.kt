package com.laaltentech.abou.myapplication.game.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "IndividualGameScore")
class IndividualGameScore{
    @PrimaryKey(autoGenerate = false)

    @SerializedName("individual_game_id")
    var game_id: String = System.currentTimeMillis().toString()

    @SerializedName("timestamp")
    var timeTaken: String? = "Zuhayr"

    @SerializedName("word")
    var word: String? = null
}