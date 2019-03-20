package com.example.homestay.ui.hotel_detail

import android.app.Activity
import android.content.Intent
import android.view.MenuItem
import android.view.View
import com.example.homestay.custom.CustomDialog

interface HotelDetailMvpPresenter {
    fun addToFavorite(context: Activity, hotelName: String, hotelAddress: String, imgUrl: String)
    fun setActionToItemClick(
        v: View?,
        itemButtonViewHotelLocation: Int,
        itemButtonViewHotelDescription: Int,
        itemButtonBookHotel: Int,
        customDialog: CustomDialog,
        hotelName: String,
        hotelAddress: String
    )
    fun setActionToSelectedItem(item: MenuItem?, itemShare: Int, itemAddToFavorite: Int, hotelName: String, hotelAddress: String, listImage: ArrayList<String>)
    fun getActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    )
}