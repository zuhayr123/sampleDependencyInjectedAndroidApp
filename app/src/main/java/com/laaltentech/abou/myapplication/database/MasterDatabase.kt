package com.laaltentech.abou.myapplication.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.laaltentech.abou.myapplication.game.data.GameData

@Database(entities =
[
    (GameData::class)
//    (FlickrImages::class), todo example to add details here
], version = 1, exportSchema = false)

@TypeConverters(DateConverter::class)

abstract class MasterDatabase : RoomDatabase() {

//    abstract fun flickDAO(): FlickDAO todo example to add daos here
}