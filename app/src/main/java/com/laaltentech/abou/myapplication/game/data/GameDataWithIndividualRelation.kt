package com.laaltentech.abou.myapplication.game.data

import androidx.room.Embedded
import androidx.room.Relation

class GameDataWithIndividualRelation {
    @Embedded
    var newGameData: GameData = GameData()

    @Relation(entityColumn = "game_id", parentColumn = "game_id")
    var imageIndividualGameScore: MutableList<IndividualGameScore> = ArrayList()
}