package com.example.homestay.ui.book_hotel

import android.support.v4.view.ViewPager
import android.support.v7.widget.AppCompatButton
import android.view.View
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import com.example.homestay.custom.ImageSlider
import me.relex.circleindicator.CircleIndicator
import java.util.ArrayList

interface BookHotelMvpPresenter {
    fun getUserInfo()
    fun setGuestAllowance(
        static_spinner: Spinner,
        imageSlider: ImageSlider,
        listImageSingleRoom: ArrayList<String>,
        listImageDoubleRoom: ArrayList<String>,
        listImageFamilyRoom: ArrayList<String>,
        vgBookHotelImageSlider: ViewPager,
        bookHotelCircleIndicator: CircleIndicator,
        etRoomQty: EditText,
        tvNumberOfGuest: TextView
    )

    fun addRoomTypeItemToSpinner(
        spinner: Spinner,
        roomTypeArray: Int,
        customSpinnerLayout: Int,
        spinnerDropDownLayout: Int
    )

    fun getTimePickerDialog(tvChooseCheckInTime: TextView, tvChooseCheckOutTime: TextView)
    fun getDatePickerDialog(
        tvChooseCheckInDate: TextView,
        tvChooseCheckOutDate: TextView,
        tvDisplayResult: TextView,
        tvNumberOfDays: TextView
    )

    fun setTimeAndDateLabelClickListener(
        v: View?,
        itemChooseCheckInDate: Int,
        itemChooseCheckOutDate: Int,
        itemChooseCheckInTime: Int,
        itemChooseCheckOutTime: Int,
        itemButtonBookNow: Int,
        tvChooseCheckInDate: TextView,
        tvChooseCheckOutDate: TextView,
        tvNumberOfDays: TextView,
        tvChooseCheckInTime: TextView,
        tvChooseCheckOutTime: TextView,
        static_spinner: Spinner,
        tvNumberOfGuest: TextView,
        etRoomQty: EditText,
        tvClientName: TextView,
        tvTotalAmount1: TextView,
        etDepositAmount: EditText,
        tvHotelName: TextView,
        tvHotelAddress: TextView
    )
}