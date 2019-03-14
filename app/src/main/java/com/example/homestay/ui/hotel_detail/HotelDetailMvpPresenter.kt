package com.example.homestay.ui.hotel_detail

import android.app.Activity

interface HotelDetailMvpPresenter {
    fun addToFavorite(context: Activity, hotelName: String, hotelAddress: String, imgUrl: String)
}