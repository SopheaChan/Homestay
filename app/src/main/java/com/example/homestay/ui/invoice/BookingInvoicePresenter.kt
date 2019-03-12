package com.example.homestay.ui.invoice

import android.app.Activity
import android.os.Build
import android.support.annotation.RequiresApi
import android.widget.TextView
import android.widget.Toast
import com.example.homestay.model.BookingInfo
import com.example.homestay.utils.GetCurrentDateTime
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.Serializable

class BookingInvoicePresenter(private val activity: Activity) : BookingInvoiceMvpPresenter {
    private lateinit var mFirebaseAuth: FirebaseAuth
    private lateinit var mFirebaseUser: FirebaseUser
    private lateinit var mDatabaseReference: DatabaseReference

    init {
        mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseUser = mFirebaseAuth.currentUser!!
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("booking")
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun getBookingDetail(
        tvCheckInDate: TextView,
        tvCheckOutDate: TextView,
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
    ) {
        val bookingInfo: BookingInfo = activity.intent.getSerializableExtra("booking_info") as BookingInfo
        val userID = mFirebaseUser.uid
        val itemID = mDatabaseReference.push().key!!
        mDatabaseReference.child(userID).child(itemID).setValue(bookingInfo)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    tvCheckInDate.text = bookingInfo.checkInDate
                    tvCheckInTime.text = bookingInfo.checkInTime
                    tvCheckOutDate.text = bookingInfo.checkOutDate
                    tvCheckOutTime.text = bookingInfo.checkInTime
                    tvRoomType.text = bookingInfo.roomType
                    tvInvoiceRoomQty.text = bookingInfo.roomQty.toString()
                    tvInvoiceNumOfGuests.text = bookingInfo.numbOfGuest.toString()
                    tvGuestName.text = bookingInfo.guestName
                    tvTotalAmount.text = bookingInfo.totalAmount.toString()
                    tvPrePaidAmount.text = bookingInfo.prepaidAmount.toString()
                    tvInvoiceNumOfDays.text = bookingInfo.numbOfDays.toString()
                    tvHotelName.text = bookingInfo.hotelName
                    tvHotelAddress.text = bookingInfo.hotelAddress
                    tvIssuedDate.text = bookingInfo.issuedDate
                } else {
                    Toast.makeText(activity.baseContext, "Failed to save data...", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(activity.baseContext, it.toString(), Toast.LENGTH_SHORT).show()
            }
    }
}