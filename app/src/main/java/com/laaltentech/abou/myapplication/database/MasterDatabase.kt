package com.laaltentech.abou.myapplication.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.laaltentech.abou.myapplication.game.data.*

@Database(entities =
[
    (GameData::class),
    (IndividualGameScore::class),
    (FacebookProfileData::class),
    (FacebookPageListData::class),
    (FacebookPageData::class)
], version = 9, exportSchema = false)

@TypeConverters(DateConverter::class)

abstract class MasterDatabase : RoomDatabase() {

    abstract fun gameDAO(): GameDAO
}