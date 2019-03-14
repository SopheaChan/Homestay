package com.example.homestay.utils

import android.icu.text.SimpleDateFormat
import android.os.Build
import android.support.annotation.RequiresApi
import java.util.*
import javax.xml.datatype.DatatypeConstants.DAYS
import android.provider.Settings.System.DATE_FORMAT
import java.text.ParseException
import java.util.concurrent.TimeUnit


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
        val strDateFormat = "dd-MMM-yyyy hh:mm a"
        val dateFormat = SimpleDateFormat(strDateFormat)
        return dateFormat.format(date)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getAddedToFavoriteListOnDate(): String{
        val date = Date()
        val strDateFormat = "dd-MMM-yyyy hh:mm:ss a"
        val dateFormat = SimpleDateFormat(strDateFormat)
        return dateFormat.format(date)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getCurrentDate(): String{
        val date = Date()
        val strDateFormat = "dd-MMM-yyyy"
        val dateFormat = SimpleDateFormat(strDateFormat)
        return dateFormat.format(date)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getCurrentTime(): String{
        val time = Date()
        val strTimeFormat = "hh:mm a"
        val dateFormat = SimpleDateFormat(strTimeFormat)
        return dateFormat.format(time)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getTomorrowDate(): String{
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val tomorrow = calendar.time
        val dateFormat = SimpleDateFormat("dd-MMM-yyyy")
        val tomorrowAsString = dateFormat.format(tomorrow)
        return tomorrowAsString
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getDaysBetweenDates(start: String, end: String): Long {
        val dateFormat = SimpleDateFormat("dd-MMM-yyyy")
        val startDate: Date
        val endDate: Date
        var numberOfDays: Long = 0
        try {
            startDate = dateFormat.parse(start)
            endDate = dateFormat.parse(end)
            val diff = endDate.time - startDate.time
            numberOfDays = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return numberOfDays
    }
}