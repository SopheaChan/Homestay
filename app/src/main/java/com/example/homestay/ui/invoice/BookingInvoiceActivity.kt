package com.example.homestay.ui.invoice

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.homestay.R

class BookingInvoiceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        setContentView(R.layout.activity_booking_detail)
    }
}
