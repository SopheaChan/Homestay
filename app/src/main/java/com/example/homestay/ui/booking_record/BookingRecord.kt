package com.example.homestay.ui.booking_record

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.WindowManager
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.homestay.R
import com.example.homestay.adapter.BookingRecordAdapter
import com.example.homestay.utils.GetCurrentDateTime
import com.example.homestay.utils.StoreCurrentUserInfo
import com.yarolegovich.discretescrollview.DiscreteScrollView
import com.yarolegovich.discretescrollview.transform.Pivot
import com.yarolegovich.discretescrollview.transform.ScaleTransformer
import kotlinx.android.synthetic.main.activity_booking_record.*
import java.util.*

class BookingRecord : AppCompatActivity() {

//    lateinit var recyclerView: RecyclerView
    lateinit var bookingRecordAdapter: BookingRecordAdapter

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        setContentView(R.layout.activity_booking_record)

        /*recyclerView = findViewById(R.id.recycler_view_booking_record)
        recyclerView.hasFixedSize()
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)*/

        bookingRecordAdapter = BookingRecordAdapter(this, StoreCurrentUserInfo.getBookingRecordList())
//        recyclerView.adapter = bookingRecordAdapter
        discreteScrollView.adapter = bookingRecordAdapter
        discreteScrollView.setSlideOnFling(true)
        discreteScrollView.setItemTransformer(
            ScaleTransformer.Builder()
                .setMaxScale(1.02f)
                .setMinScale(0.8f)
                .setPivotX(Pivot.X.CENTER) // CENTER is a default one
                .setPivotY(Pivot.Y.BOTTOM) // CENTER is a default one
                .build()
        )
        bookingRecordAdapter.notifyDataSetChanged()

        loadUser()
        discreteScrollView.addOnItemChangedListener { viewHolder, adapterPosition ->
            val checkInDate: String ?= StoreCurrentUserInfo.getBookingRecordList()[adapterPosition].checkInDate
            getCountLeftDays(checkInDate!!)
        }
    }

    private fun loadUser() {
        Glide.with(this).load(StoreCurrentUserInfo.getUser().uProfile).into(imgBookingRecordProfilePicture)
        tvBookingRecordUserName.text = StoreCurrentUserInfo.getUser().userBasicInfo?.name
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getCountLeftDays(checkInDate: String){
        var daysLeft = GetCurrentDateTime.getDaysBetweenDates(GetCurrentDateTime.getCurrentDate(), checkInDate)
        if (daysLeft <0 ){
            daysLeft *= -1
            tvDaysLeft.text = "$daysLeft days ago"
        } else{
            tvDaysLeft.text = "$daysLeft days left"
        }
    }
}

