package com.example.homestay.ui.book_hotel

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.example.homestay.R
import com.example.homestay.model.BookingInfo
import com.example.homestay.ui.invoice.BookingInvoiceActivity
import com.example.homestay.utils.GetCurrentDateTime
import com.example.homestay.utils.GetMonth
import kotlinx.android.synthetic.main.activity_book_hotel.*


class BookHotelActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mCheckInDate: String
    private lateinit var mCheckOutDate: String
    private lateinit var mCheckInTime: String
    private lateinit var mCheckOutTime: String
    private var mNumbOfDays: Int = 0
    private lateinit var mRoomType: String
    private var mNumbOfGuest: Int = 0
    private var mRoomQty: Int = 0
    private lateinit var mGuestName: String
    private lateinit var mTotalAmount: String
    private lateinit var mPrePaidAmount: String
    private lateinit var mHotelName: String
    private lateinit var mHotelAddress: String
    private lateinit var mIssuedDate: String

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
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
        tvNumberOfDays.text = "${numberOfDays.toString()}"
        tvHotelName.text = mHotelName
        tvHotelAddress.text = mHotelAddress


        tvChooseCheckInDate.setOnClickListener(this)
        tvChooseCheckOutDate.setOnClickListener(this)
        tvChooseCheckInTime.setOnClickListener(this)
        btnBookNow.setOnClickListener(this)
        btnRemoveGuest.setOnClickListener(this)
        btnAddGuest.setOnClickListener(this)

        addRoomTypeSpinner()
    }

    private fun addRoomTypeSpinner() {
        val spinner: Spinner = findViewById(R.id.static_spinner)
        val staticAdapter = ArrayAdapter
            .createFromResource(
                this, R.array.array_room_type,
                R.layout.custom_spinner_layout_select_room_type
            )

        // Specify the layout to use when the list of choices appears
        staticAdapter
            .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Apply the adapter to the spinner
        spinner.adapter = staticAdapter
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onClick(v: View?) {
        var numbOfGuests: Int = Integer.parseInt(tvNumberOfGuest.text.toString())
        when (v?.id) {

            R.id.tvChooseCheckInDate -> {
                displayDatePicker(tvChooseCheckInDate)
            }
            R.id.tvChooseCheckOutDate -> {
                displayDatePicker(tvChooseCheckOutDate)
            }
            R.id.tvChooseCheckInTime -> {
                displayTimePicker()
            }
            R.id.btnBookNow -> {
                mIssuedDate = GetCurrentDateTime.getCurrentTimeUsingDate()
                mCheckInDate = tvChooseCheckInDate.text.toString().trim()
                mCheckOutDate = tvChooseCheckOutDate.text.toString().trim()
                mNumbOfDays = Integer.parseInt(tvNumberOfDays.text.toString().trim())
                mCheckInTime = tvChooseCheckInTime.text.toString().trim()
                mCheckOutTime = tvChooseCheckOutTime.text.toString().trim()
                mRoomType = static_spinner.selectedItem.toString().trim()
                mNumbOfGuest = Integer.parseInt(tvNumberOfGuest.text.toString().trim())
                mRoomQty = Integer.parseInt(etRoomQty.text.toString().trim())
                mGuestName = tvClientName.text.toString().trim()
                mTotalAmount = tvTotalAmount1.text.toString().trim()
                mPrePaidAmount = etDepositeAmount.text.toString().trim()
                val bookingInfo = BookingInfo(
                    mCheckInDate,
                    mCheckOutDate,
                    mNumbOfDays,
                    mRoomType,
                    mNumbOfGuest,
                    mRoomQty,
                    mGuestName,
                    mTotalAmount,
                    etDepositeAmount.text.toString(),
                    mCheckInTime,
                    mCheckOutTime,
                    mHotelName,
                    mHotelAddress,
                    mIssuedDate
                )

                val intent = Intent(this@BookHotelActivity, BookingInvoiceActivity::class.java)
                intent.putExtra("booking_info", bookingInfo)
                startActivity(intent)
                finish()
            }
            R.id.btnAddGuest -> {
                mRoomType = static_spinner.selectedItem.toString()
                numbOfGuests += 1
                tvNumberOfGuest.text = numbOfGuests.toString()
                /*when (mRoomType) {
                    "single room" -> {
                        if (numbOfGuests < 3) {
                            numbOfGuests += 1
                            tvNumberOfGuest.text = numbOfGuests.toString()
                        } else {
                            Toast.makeText(this, "More than 3 people is not allowed for this room", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    "double room" -> {
                        if (numbOfGuests < 4) {
                            numbOfGuests += 1
                            tvNumberOfGuest.text = numbOfGuests.toString()
                        } else {
                            Toast.makeText(this, "More than 4 people is not allowed for this room", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    "family room" -> {
                        if (numbOfGuests < 7) {
                            numbOfGuests += 1
                            tvNumberOfGuest.text = numbOfGuests.toString()
                        } else {
                            Toast.makeText(this, "More than 7 people is not allowed for this room", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }*/
            }
            R.id.btnRemoveGuest -> {
                if (numbOfGuests > 0) {
                    numbOfGuests -= 1
                    tvNumberOfGuest.text = numbOfGuests.toString()
                }
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun displayDatePicker(tvDisplayResult: TextView) {
        val c = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR)
        val mMonth = c.get(Calendar.MONTH)
        val mDay = c.get(Calendar.DAY_OF_MONTH)


        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                tvDisplayResult.text = ("$dayOfMonth-${GetMonth.getMonth(monthOfYear + 1)}-$year")
                val numberOfDays = GetCurrentDateTime.getDaysBetweenDates(
                    tvChooseCheckInDate.text.toString(),
                    tvChooseCheckOutDate.text.toString()
                )
                tvNumberOfDays.text = ("$numberOfDays")
            }, mYear, mMonth, mDay
        )
        datePickerDialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun displayTimePicker() {
        val c = Calendar.getInstance()
        val mHour = c.get(Calendar.HOUR_OF_DAY)
        val mMinute = c.get(Calendar.MINUTE)
        val mAM_PM = c.get(Calendar.AM_PM)
        val mAMPM = if (mAM_PM == Calendar.AM) "AM" else "PM"

        // Launch Time Picker Dialog
        val timePickerDialog = TimePickerDialog(
            this,
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                tvChooseCheckInTime.text = ("$hourOfDay:$minute $mAMPM")
                tvChooseCheckOutTime.text = ("$hourOfDay:$minute $mAMPM")
            },
            mHour,
            mMinute, false
        )
        timePickerDialog.show()
    }
}
