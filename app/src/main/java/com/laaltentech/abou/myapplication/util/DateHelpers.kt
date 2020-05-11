package com.laaltentech.abou.myapplication.util

import java.text.SimpleDateFormat
import java.util.*

class DateHelpers {

    fun getConvertedDate(timeStamp: String?, format: String): String {
        var timeStamp1 = System.currentTimeMillis()
        if (timeStamp != null) {
            timeStamp1 = timeStamp.toLong()
        }

        val dateFormat = SimpleDateFormat(format, Locale.ENGLISH)
        val mDate = Date(timeStamp1)
        return dateFormat.format(mDate)
    }
}