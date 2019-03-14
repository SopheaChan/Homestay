package com.example.homestay.ui.invoice

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.homestay.R
import kotlinx.android.synthetic.main.activity_booking_invoice.*

class BookingInvoiceActivity : AppCompatActivity() {
    val bookingInvoiceMvpPresenter: BookingInvoiceMvpPresenter = BookingInvoicePresenter(this@BookingInvoiceActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.anim_sliding_in_right, R.anim.anim_sliding_out_left)
        setContentView(R.layout.activity_booking_invoice)

        loadBookingInfo()
    }

    private fun loadBookingInfo() {
        bookingInvoiceMvpPresenter.getBookingDetail(tvCheckInDate, tvCheckOutData, tvCheckInTime, tvCheckOutTime,
            tvRoomType, tvInvoiceRoomQty, tvInvoiceNumOfGuests, tvGuestName, tvTotalAmount, tvPrePaidAmount,
            tvInvoiceNumOfDaysBookingList, tvHotelNameBookingList, tvHotelLocation, tvIssuedDate)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.anim_sliding_in_left, R.anim.anim_sliding_out_right)
    }
}
