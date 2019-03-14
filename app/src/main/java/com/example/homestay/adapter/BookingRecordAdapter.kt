package com.example.homestay.adapter

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.homestay.R
import com.example.homestay.model.BookingInfo
import org.w3c.dom.Text

class BookingRecordAdapter(private val activity: Activity, private val bookingRecordList: ArrayList<BookingInfo>) :
    RecyclerView.Adapter<BookingRecordAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view: View = LayoutInflater.from(activity.baseContext).inflate(R.layout.layout_booking_list, p0, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return bookingRecordList.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val bookingRecord: BookingInfo = bookingRecordList[position]

        viewHolder.tvCheckInDate.text = bookingRecord.checkInDate
        viewHolder.tvCheckInTime.text = bookingRecord.checkInTime
        viewHolder.tvCheckOutDate.text = bookingRecord.checkOutDate
        viewHolder.tvCheckOutTime.text = bookingRecord.checkOutTime
        viewHolder.tvNumOfDays.text = bookingRecord.numbOfDays.toString()
        viewHolder.tvRoomType.text = bookingRecord.roomType
        viewHolder.tvRoomQty.text = bookingRecord.roomQty.toString()
        viewHolder.tvNumOfGuest.text = bookingRecord.numbOfGuest.toString()
        viewHolder.tvHotelName.text = bookingRecord.hotelName
        viewHolder.tvHotelAddress.text = bookingRecord.hotelAddress
        viewHolder.tvGuestName.text = bookingRecord.guestName
        viewHolder.tvTotalAmount.text = bookingRecord.totalAmount
        viewHolder.tvPrePaidAmount.text = bookingRecord.prepaidAmount
        viewHolder.tvIssuedDate.text = bookingRecord.issuedDate
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvCheckInDate: TextView = view.findViewById(R.id.tvBookingRecordCheckInDate)
        val tvCheckInTime: TextView = view.findViewById(R.id.tvBookingRecordCheckInTime)
        val tvCheckOutDate: TextView = view.findViewById(R.id.tvBookingRecordCheckOutData)
        val tvCheckOutTime: TextView = view.findViewById(R.id.tvBookingRecordCheckOutTime)
        val tvNumOfDays: TextView = view.findViewById(R.id.tvBookingRecordNumOfDays)
        val tvRoomType: TextView = view.findViewById(R.id.tvBookingRecordRoomType)
        val tvRoomQty: TextView = view.findViewById(R.id.tvBookingRecordRoomQty)
        val tvNumOfGuest: TextView = view.findViewById(R.id.tvBookingRecordNumOfGuests)
        val tvHotelName: TextView = view.findViewById(R.id.tvBookingRecordHotelName)
        val tvHotelAddress: TextView = view.findViewById(R.id.tvBookingRecordHotelLocation)
        val tvGuestName: TextView = view.findViewById(R.id.tvBookingRecordGuestName)
        val tvTotalAmount: TextView = view.findViewById(R.id.tvBookingRecordTotalAmount)
        val tvPrePaidAmount: TextView = view.findViewById(R.id.tvBookingRecordPrePaidAmount)
        val tvIssuedDate: TextView = view.findViewById(R.id.tvBookingRecordIssuedDate)
//        val tvInvoiceNo: TextView = view.findViewById(R.id.tvBookingRecordInvoiceNo)

    }
}