package com.example.homestay.ui.booking_record

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.WindowManager
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.homestay.R
import com.example.homestay.adapter.BookingRecordAdapter
import com.example.homestay.utils.StoreCurrentUserInfo
import kotlinx.android.synthetic.main.activity_booking_record.*

class BookingRecord : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var bookingRecordAdapter: BookingRecordAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        setContentView(R.layout.activity_booking_record)

        recyclerView = findViewById(R.id.recycler_view_booking_record)
        recyclerView.hasFixedSize()
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        bookingRecordAdapter = BookingRecordAdapter(this, StoreCurrentUserInfo.getBookingRecordList())
        recyclerView.adapter = bookingRecordAdapter
        bookingRecordAdapter.notifyDataSetChanged()

        loadUser()
    }

    private fun loadUser() {
        Glide.with(this).load(StoreCurrentUserInfo.getUser().uProfile).into(imgBookingRecordProfilePicture)
        tvBookingRecordUserName.text = StoreCurrentUserInfo.getUser().userBasicInfo?.name
    }
}
