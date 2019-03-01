package com.example.homestay.ui.home

import android.app.Activity
import android.content.Context
import android.widget.ImageView
import com.example.homestay.adapter.HomeAdapter
import com.example.homestay.model.HotelData

interface HomeMvpPresenter {
    fun setDataToList(hotelList: MutableList<HotelData>, adapter: HomeAdapter)
    fun onRecyclerImagesClicked(context: Activity, hotelData: HotelData, imageView: ImageView)
}