package com.example.homestay.utils

import android.icu.text.SimpleDateFormat
import android.os.Build
import android.support.annotation.RequiresApi
import java.util.*


object GetCurrentDateTime {

    /*fun getCurrentTimeUsingCalendar() {
        val cal = Calendar.getInstance()
        val date = cal.getTime
        val dateFormat = SimpleDateFormat("HH:mm:ss")
        val formattedDate = dateFormat.format(date)
        println("Current time of the day using Calendar - 24 hour format: $formattedDate")
    }*/

    @RequiresApi(Build.VERSION_CODES.N)
    fun getCurrentTimeUsingDate(): String{
        val date = Date()
        val strDateFormat = "dd-MM-yyyy hh:mm:ss a"
        val dateFormat = SimpleDateFormat(strDateFormat)
        return dateFormat.format(date)
    }
}