package com.laaltentech.abou.myapplication.database

import androidx.databinding.ObservableField
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.laaltentech.abou.myapplication.game.data.IndividualGameScore
import java.sql.Date

class DateConverter {
    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return if (timestamp == null) null else Date(timestamp)
    }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun toObserableDate(timestamp: Long?): ObservableField<Date?>? {
        return if (timestamp == null) null else {
            val date:  ObservableField<Date?>? = ObservableField()
            date?.set(Date(timestamp))
            return date
        }
    }

    @TypeConverter
    fun toObserableTimestamp(date: ObservableField<Date?>?): Long? {
        return date?.get()?.time
    }

    @TypeConverter
    fun listToJson(value: MutableList<String?>?): String? {
        return Gson().toJson(value)
    }

    //todo added this code as an example to use in future refrences.
    @TypeConverter
    fun listToJsonVarity(value: MutableList<IndividualGameScore?>?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToListVarity(value: String?): MutableList<IndividualGameScore?>? {

        val objects = Gson().fromJson(value, Array<IndividualGameScore?>::class.java)
        val list = objects?.toMutableList()
        return list
    }

    @TypeConverter
    fun jsonToList(value: String?): MutableList<String?>? {

        val objects = Gson().fromJson(value, Array<String?>::class.java)
        val list = objects?.toMutableList()
        return list
    }

}