package com.example.homestay.ui.book_hotel

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.view.ViewPager
import android.support.v7.widget.AppCompatButton
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import com.example.homestay.R
import com.example.homestay.custom.ImageSlider
import com.example.homestay.model.BookingInfo
import com.example.homestay.ui.invoice.BookingInvoiceActivity
import com.example.homestay.utils.GetCurrentDateTime
import com.example.homestay.utils.GetMonth
import kotlinx.android.synthetic.main.activity_book_hotel.*
import me.relex.circleindicator.CircleIndicator
import java.util.*

class BookHotelPresenter(private val activity: Activity) : BookHotelMvpPresenter {
    override fun getUserInfo() {

    }

    override fun setGuestAllowance(
        static_spinner: Spinner,
        imageSlider: ImageSlider,
        listImageSingleRoom: ArrayList<String>,
        listImageDoubleRoom: ArrayList<String>,
        listImageFamilyRoom: ArrayList<String>,
        vgBookHotelImageSlider: ViewPager,
        bookHotelCircleIndicator: CircleIndicator,
        etRoomQty: EditText,
        tvNumberOfGuest: TextView
    ) {
        static_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedRoomType: String = static_spinner.getItemAtPosition(position).toString()
                when (selectedRoomType.toLowerCase()) {
                    "single room" -> {
                        checkGuestAllowancePerRoomOnRoomTypeChanged(3, etRoomQty, tvNumberOfGuest)
                        imageSlider.setImageSlider(
                            activity,
                            listImageSingleRoom,
                            vgBookHotelImageSlider,
                            bookHotelCircleIndicator
                        )
                    }
                    "double room" -> {
                        checkGuestAllowancePerRoomOnRoomTypeChanged(5, etRoomQty, tvNumberOfGuest)
                        imageSlider.setImageSlider(
                            activity,
                            listImageDoubleRoom,
                            vgBookHotelImageSlider,
                            bookHotelCircleIndicator
                        )
                    }
                    "family room" -> {
                        checkGuestAllowancePerRoomOnRoomTypeChanged(7, etRoomQty, tvNumberOfGuest)
                        imageSlider.setImageSlider(
                            activity,
                            listImageFamilyRoom,
                            vgBookHotelImageSlider,
                            bookHotelCircleIndicator
                        )
                    }
                }
            }

        }

        etRoomQty.text.toString().trim()
        etRoomQty.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    when (static_spinner.selectedItem.toString().toLowerCase()) {
                        "single room" -> {
                            checkGuestAllowancePerRoomOnRoomQuantityChanged(3, s, tvNumberOfGuest)
                        }
                        "double room" -> {
                            checkGuestAllowancePerRoomOnRoomQuantityChanged(5, s, tvNumberOfGuest)
                        }
                        "family room" -> {
                            checkGuestAllowancePerRoomOnRoomQuantityChanged(7, s, tvNumberOfGuest)
                        }
                    }
                } else {
                    when (static_spinner.selectedItem.toString().toLowerCase()) {
                        "single room" -> {
                            tvNumberOfGuest.text = "3"
                        }
                        "double room" -> {
                            tvNumberOfGuest.text = "5"
                        }
                        "family room" -> {
                            tvNumberOfGuest.text = "7"
                        }
                    }
                }
            }

        })
    }

    fun checkGuestAllowancePerRoomOnRoomQuantityChanged(
        allowanceQuantity: Int,
        s: CharSequence,
        tvNumberOfGuest: TextView
    ) {
        tvNumberOfGuest.text =
            (Integer.parseInt(s.toString()) * allowanceQuantity).toString()
    }

    fun checkGuestAllowancePerRoomOnRoomTypeChanged(
        allowanceQuantity: Int,
        etRoomQty: EditText,
        tvNumberOfGuest: TextView
    ) {
        if (!etRoomQty.text.toString().isEmpty()) {
            tvNumberOfGuest.text =
                String.format(Locale.US, "%d", allowanceQuantity * Integer.parseInt(etRoomQty.text.toString()))
        } else {
            tvNumberOfGuest.text = allowanceQuantity.toString()
        }
    }

    override fun addRoomTypeItemToSpinner(
        spinner: Spinner,
        roomTypeArray: Int,
        customSpinnerLayout: Int,
        spinnerDropDownLayout: Int
    ) {
        val staticAdapter = ArrayAdapter
            .createFromResource(
                activity.baseContext, roomTypeArray,
                customSpinnerLayout
            )
        // Specify the layout to use when the list of choices appears
        staticAdapter
            .setDropDownViewResource(spinnerDropDownLayout)

        // Apply the adapter to the spinner
        spinner.adapter = staticAdapter
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun getTimePickerDialog(tvChooseCheckInTime: TextView, tvChooseCheckOutTime: TextView) {
        val c = Calendar.getInstance()
        val mHour = c.get(Calendar.HOUR_OF_DAY)

        val mMinute = c.get(Calendar.MINUTE)
        val mAM_PM = c.get(Calendar.AM_PM)
        val mAMPM = if (mAM_PM == Calendar.AM) "AM" else "PM"

        // Launch Time Picker Dialog
        val timePickerDialog = TimePickerDialog(
            activity,
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                tvChooseCheckInTime.text = ("$hourOfDay:$minute $mAMPM")
                tvChooseCheckOutTime.text = ("$hourOfDay:$minute $mAMPM")
            },
            mHour,
            mMinute, false
        )
        timePickerDialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun getDatePickerDialog(
        tvChooseCheckInDate: TextView, tvChooseCheckOutDate: TextView,
        tvDisplayResult: TextView,
        tvNumberOfDays: TextView
    ) {
        val c = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR)
        val mMonth = c.get(Calendar.MONTH)
        val mDay = c.get(Calendar.DAY_OF_MONTH)


        val datePickerDialog = DatePickerDialog(
            activity,
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
    override fun setTimeAndDateLabelClickListener(
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
        etDepositAmount: EditText
    ) {
        lateinit var mCheckInDate: String
        lateinit var mCheckOutDate: String
        lateinit var mCheckInTime: String
        lateinit var mCheckOutTime: String
        var mNumbOfDays: Int = 0
        lateinit var mRoomType: String
        var mNumbOfGuest: Int = 0
        lateinit var mRoomQty: String
        lateinit var mGuestName: String
        lateinit var mTotalAmount: String
        lateinit var mPrePaidAmount: String
        lateinit var mHotelName: String
        lateinit var mHotelAddress: String
        lateinit var mIssuedDate: String
        when (v?.id) {

            R.id.tvChooseCheckInDate -> {
                getDatePickerDialog(tvChooseCheckInDate, tvChooseCheckOutDate, tvChooseCheckInDate, tvNumberOfDays)
            }
            R.id.tvChooseCheckOutDate -> {
                getDatePickerDialog(tvChooseCheckInDate, tvChooseCheckOutDate, tvChooseCheckOutDate, tvNumberOfDays)
            }
            R.id.tvChooseCheckInTime -> {
                getTimePickerDialog(tvChooseCheckInTime, tvChooseCheckOutTime)
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
                mRoomQty = etRoomQty.text.toString().trim()
                mGuestName = tvClientName.text.toString().trim()
                mTotalAmount = tvTotalAmount1.text.toString().trim()
                mPrePaidAmount = etDepositAmount.text.toString().trim()
                if (mRoomQty.isEmpty() || mPrePaidAmount.isEmpty()) {
                    if (mRoomQty.isEmpty()) {
                        etRoomQty.error = "How many room do you want to book with us?"
                    }
                    if (mPrePaidAmount.isEmpty()) {
                        etDepositAmount.error = "Please input your deposit amount."
                    }

                } else {
                    val bookingInfo = BookingInfo(
                        mCheckInDate,
                        mCheckOutDate,
                        mNumbOfDays,
                        mRoomType,
                        mNumbOfGuest,
                        Integer.parseInt(mRoomQty),
                        mGuestName,
                        mTotalAmount,
                        etDepositAmount.text.toString(),
                        mCheckInTime,
                        mCheckOutTime,
                        mHotelName,
                        mHotelAddress,
                        mIssuedDate
                    )

                    val intent = Intent(activity.baseContext, BookingInvoiceActivity::class.java)
                    intent.putExtra("booking_info", bookingInfo)
                    activity.startActivity(intent)
                    activity.finish()
                }
            }
        }
    }
}