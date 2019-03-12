package com.example.homestay.ui.invoice

import android.app.Activity
import android.widget.TextView

interface BookingInvoiceMvpPresenter {
    fun getBookingDetail(
        tvCheckInDate: TextView,
        tvCheckOutData: TextView,
        tvCheckInTime: TextView,
        tvCheckOutTime: TextView,
        tvRoomType: TextView,
        tvInvoiceRoomQty: TextView,
        tvInvoiceNumOfGuests: TextView,
        tvGuestName: TextView,
        tvTotalAmount: TextView,
        tvPrePaidAmount: TextView,
        tvInvoiceNumOfDays: TextView,
        tvHotelName: TextView,
        tvHotelAddress: TextView,
        tvIssuedDate: TextView
    )
}