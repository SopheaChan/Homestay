package com.example.homestay.ui.book_hotel

import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.widget.Spinner
import com.example.homestay.R
import com.example.homestay.custom.ImageSlider
import com.example.homestay.utils.GetCurrentDateTime
import com.example.homestay.utils.RoomTypeInfo
import com.example.homestay.utils.StoreCurrentUserInfo
import kotlinx.android.synthetic.main.activity_book_hotel.*


class BookHotelActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mHotelName: String
    private lateinit var mHotelAddress: String

    private val imageSlider: ImageSlider = ImageSlider()
    private val bookHotelMvpPresenter: BookHotelMvpPresenter = BookHotelPresenter(this@BookHotelActivity)

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.anim_sliding_in_right, R.anim.anim_sliding_out_left)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        setContentView(R.layout.activity_book_hotel)

        val numberOfDays = GetCurrentDateTime.getDaysBetweenDates(
            GetCurrentDateTime.getCurrentDate(),
            GetCurrentDateTime.getTomorrowDate()
        )
        mHotelName = intent.getStringExtra("hotel_name")
        mHotelAddress = intent.getStringExtra("hotel_address")
        tvChooseCheckInDate.text = GetCurrentDateTime.getCurrentDate()
        tvChooseCheckInTime.text = GetCurrentDateTime.getCurrentTime()
        tvChooseCheckOutDate.text = GetCurrentDateTime.getTomorrowDate()
        tvChooseCheckOutTime.text = GetCurrentDateTime.getCurrentTime()
        tvNumberOfDays.text = "$numberOfDays"
        tvHostingHotelName.text = mHotelName
        tvHotelAddress.text = mHotelAddress
        tvClientName.text = StoreCurrentUserInfo.getUser().userBasicInfo?.name ?: "N/A"


        tvChooseCheckInDate.setOnClickListener(this)
        tvChooseCheckOutDate.setOnClickListener(this)
        tvChooseCheckInTime.setOnClickListener(this)
        btnBookNow.setOnClickListener(this)

        setGuestAllowance()
        addRoomTypeSpinner()
    }

    private fun setGuestAllowance() {
        bookHotelMvpPresenter.setGuestAllowance(
            static_spinner,
            imageSlider,
            RoomTypeInfo.getListImageSingleRoom(),
            RoomTypeInfo.getListImageDoubleRoom(),
            RoomTypeInfo.getListImageFamilyRoom(),
            vgBookHotelImageSlider,
            bookHotelCircleIndicator,
            etRoomQty,
            tvNumberOfGuest
        )
    }

    private fun addRoomTypeSpinner() {

        val spinner: Spinner = findViewById(R.id.static_spinner)
        bookHotelMvpPresenter.addRoomTypeItemToSpinner(
            spinner,
            R.array.array_room_type,
            R.layout.custom_spinner_layout_select_room_type,
            android.R.layout.simple_spinner_dropdown_item
        )
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onClick(v: View?) {
        bookHotelMvpPresenter.setTimeAndDateLabelClickListener(
            v,
            R.id.tvChooseCheckInDate,
            R.id.tvChooseCheckOutDate,
            R.id.tvChooseCheckInTime,
            R.id.tvChooseCheckOutTime,
            R.id.btnBookNow,
            tvChooseCheckInDate,
            tvChooseCheckOutDate,
            tvNumberOfDays,
            tvChooseCheckInTime,
            tvChooseCheckOutTime,
            static_spinner,
            tvNumberOfGuest,
            etRoomQty,
            tvClientName,
            tvTotalAmount1,
            etDepositeAmount
        )
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.anim_sliding_in_left, R.anim.anim_sliding_out_right)
    }
}
