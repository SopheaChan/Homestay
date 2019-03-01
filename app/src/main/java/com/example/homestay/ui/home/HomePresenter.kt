package com.example.homestay.ui.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v4.app.ActivityOptionsCompat
import android.widget.ImageView
import com.example.homestay.adapter.HomeAdapter
import com.example.homestay.model.HotelData
import com.example.homestay.ui.view_detail.HotelDetailActivity

class HomePresenter : HomeMvpPresenter {
    override fun setDataToList(listHotel: MutableList<HotelData>, homeAdapter: HomeAdapter) {
        val hotelData1 = HotelData("https://images.pexels.com/photos/271639/pexels-photo-271639.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940",
            "Lava Hotel", "st. 26DC, New York, USA")
        val hotelData2 = HotelData("https://images.pexels.com/photos/167533/pexels-photo-167533.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=500&w=500",
            "The Park Hotel", "st. 123AY, Athene, Greece")
        val hotelData3 = HotelData("https://images.pexels.com/photos/97083/pexels-photo-97083.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=500&w=500",
            "The Light Hotel", "st. 56NB, London, England")
        val hotelData4 = HotelData("https://images.pexels.com/photos/271639/pexels-photo-271639.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940",
            "Lava Hotel", "st. 26DC, New York, USA")
        val hotelData5 = HotelData("https://images.pexels.com/photos/167533/pexels-photo-167533.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=500&w=500",
            "The Park Hotel", "st. 123AY, Athene, Greece")
        val hotelData6 = HotelData("https://images.pexels.com/photos/97083/pexels-photo-97083.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=500&w=500",
            "The Light Hotel", "st. 56NB, London, England")
        listHotel.add(hotelData1)
        listHotel.add(hotelData2)
        listHotel.add(hotelData3)
        listHotel.add(hotelData4)
        listHotel.add(hotelData5)
        listHotel.add(hotelData6)
        homeAdapter.notifyDataSetChanged()
    }

    override fun onRecyclerImagesClicked(context: Activity, hotelData: HotelData, imageView: ImageView) {
        val intent = Intent(context.baseContext, HotelDetailActivity::class.java)
        intent.putExtra("hotel_name", hotelData.hotelName)
        intent.putExtra("hotel_address", hotelData.hotelAddress)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            context,
            imageView, imageView.transitionName
        )
        context.startActivity(intent, options.toBundle())
    }
}