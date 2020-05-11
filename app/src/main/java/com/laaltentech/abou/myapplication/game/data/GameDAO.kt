package com.laaltentech.abou.myapplication.game.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GameDAO{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGameData(signUpData: GameData)

    @Query("SELECT * FROM GameData WHERE game_id = :gameId")
    fun loadAll(gameId : String) : LiveData<GameDataWithIndividualRelation>
}