package com.laaltentech.abou.myapplication.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.laaltentech.abou.myapplication.facebook.data.*

@Database(entities =
[
    (FacebookProfileData::class),
    (FacebookPageListData::class),
    (FacebookPageData::class)
], version = 10, exportSchema = false)

@TypeConverters(DateConverter::class)

abstract class MasterDatabase : RoomDatabase() {

    abstract fun facebookDao(): FacebookDAO
}