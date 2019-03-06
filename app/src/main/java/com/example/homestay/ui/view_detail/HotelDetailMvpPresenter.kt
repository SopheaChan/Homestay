package com.example.homestay.ui.view_detail

import android.app.Activity

interface HotelDetailMvpPresenter {
    fun addToFavorite(context: Activity, hotelName: String, hotelAddress: String)
}