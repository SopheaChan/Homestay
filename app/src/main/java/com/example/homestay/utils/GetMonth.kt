package com.example.homestay.utils

object GetMonth {
    fun getMonth(month: Int): String{
        var monthName: String
        when(month){
            1 -> monthName = "Jan"
            2 -> monthName = "Feb"
            3 -> monthName = "Mar"
            4 -> monthName = "Apr"
            5 -> monthName = "May"
            6 -> monthName = "Jun"
            7 -> monthName = "July"
            8 -> monthName = "Aug"
            9 -> monthName = "Sep"
            10 -> monthName = "Oct"
            11 -> monthName = "Nov"
            12 -> monthName = "Dec"
            else -> monthName = "Error"
        }
        return monthName
    }
}