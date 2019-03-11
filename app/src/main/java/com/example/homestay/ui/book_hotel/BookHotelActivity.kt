package com.example.homestay.ui.book_hotel

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.example.homestay.R
import com.example.homestay.utils.GetCurrentDateTime
import com.example.homestay.utils.GetMonth
import kotlinx.android.synthetic.main.activity_book_hotel.*
import android.widget.ArrayAdapter
import com.example.homestay.custom.OptionDialog
import com.example.homestay.ui.invoice.BookingInvoiceActivity


class BookHotelActivity : AppCompatActivity(), View.OnClickListener {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        setContentView(R.layout.activity_book_hotel)

        val numberOfDays = GetCurrentDateTime.getDaysBetweenDates(
            GetCurrentDateTime.getCurrentDate(),
            GetCurrentDateTime.getTomorrowDate()
        )
        tvChooseCheckInDate.text = GetCurrentDateTime.getCurrentDate()
        tvChooseCheckInTime.text = GetCurrentDateTime.getCurrentTime()
        tvChooseCheckOutDate.text = GetCurrentDateTime.getTomorrowDate()
        tvChooseCheckOutTime.text = GetCurrentDateTime.getCurrentTime()
        tvNumberOfDays.text = "${numberOfDays.toString()} nights"
        Toast.makeText(this, GetCurrentDateTime.getCurrentDate(), Toast.LENGTH_SHORT).show()

        tvChooseCheckInDate.setOnClickListener(this)
        tvChooseCheckOutDate.setOnClickListener(this)
        tvChooseCheckInTime.setOnClickListener(this)
        tvNumberOfGuests.setOnClickListener(this)
        btnBookNow.setOnClickListener(this)

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
            R.id.tvNumberOfGuests -> {
                val dialog = OptionDialog(this@BookHotelActivity)
                dialog.displayDialog()
            }
            R.id.btnBookNow -> {
                startActivity(Intent(this@BookHotelActivity, BookingInvoiceActivity::class.java))
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun displayDatePicker(tvDisplayResult: TextView) {
        val c = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR)
        val mMonth = c.get(Calendar.MONTH)
        val mDay = c.get(Calendar.DAY_OF_MONTH)



        val datePickerDialog = DatePickerDialog(this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                tvDisplayResult.text = ("$dayOfMonth-${GetMonth.getMonth(monthOfYear+1)}-$year")
                val numberOfDays = GetCurrentDateTime.getDaysBetweenDates(tvChooseCheckInDate.text.toString(),
                    tvChooseCheckOutDate.text.toString())
                tvNumberOfDays.text = ("$numberOfDays Nights")
            }, mYear, mMonth, mDay)
        datePickerDialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun displayTimePicker(){
        val c = Calendar.getInstance()
        val mHour = c.get(Calendar.HOUR_OF_DAY)
        val mMinute = c.get(Calendar.MINUTE)
        val mAM_PM = c.get(Calendar.AM_PM)
        val mAMPM = if (mAM_PM == Calendar.AM) "AM" else "PM"

        // Launch Time Picker Dialog
        val timePickerDialog = TimePickerDialog(this,
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute -> tvChooseCheckInTime.text = ("$hourOfDay:$minute $mAMPM")
            tvChooseCheckOutTime.text = ("$hourOfDay:$minute $mAMPM")},
            mHour,
            mMinute, false
        )
        timePickerDialog.show()
    }
}
