package com.laaltentech.abou.myapplication.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.laaltentech.abou.myapplication.game.data.GameDAO
import com.laaltentech.abou.myapplication.game.data.GameData
import com.laaltentech.abou.myapplication.game.data.IndividualGameScore

@Database(entities =
[
    (GameData::class),
    (IndividualGameScore::class)
], version = 1, exportSchema = false)

@TypeConverters(DateConverter::class)

abstract class MasterDatabase : RoomDatabase() {

    abstract fun gameDAO(): GameDAO
}