package com.example.homestay.ui.book_hotel

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import com.example.homestay.R
import com.example.homestay.custom.ImageSlider
import com.example.homestay.model.BookingInfo
import com.example.homestay.ui.invoice.BookingInvoiceActivity
import com.example.homestay.utils.GetCurrentDateTime
import com.example.homestay.utils.GetMonth
import com.example.homestay.utils.StoreCurrentUserInfo
import kotlinx.android.synthetic.main.activity_book_hotel.*
import java.util.*


class BookHotelActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mCheckInDate: String
    private lateinit var mCheckOutDate: String
    private lateinit var mCheckInTime: String
    private lateinit var mCheckOutTime: String
    private var mNumbOfDays: Int = 0
    private lateinit var mRoomType: String
    private var mNumbOfGuest: Int = 0
    private lateinit var mRoomQty: String
    private lateinit var mGuestName: String
    private lateinit var mTotalAmount: String
    private lateinit var mPrePaidAmount: String
    private lateinit var mHotelName: String
    private lateinit var mHotelAddress: String
    private lateinit var mIssuedDate: String

    private var listImageSingleRoom: ArrayList<String> = ArrayList()
    private var listImageDoubleRoom: ArrayList<String> = ArrayList()
    private var listImageFamilyRoom: ArrayList<String> = ArrayList()

    private val imageSlider: ImageSlider = ImageSlider()

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

        listImageSingleRoom.add("https://hotelpresident.hu/application/files/cache/thumbnails/237548576125a688705f0e44b520c324.jpg")
        listImageSingleRoom.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR91Ui-vuuhIiEpdTEez4YvHtekHYxWqLQDo-Y8wx_JPzFH3KSo")
        listImageSingleRoom.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTAvm3FFO7oTJgXkTFo7dsf6FPfdsmy0HcyLLVpYrDE-D1LM6KZ")
        listImageSingleRoom.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTkcKiFife8NWR33T2BV6J8-UMO-GewK6BG4MNBrf-Ei6RhaVXt")

        listImageDoubleRoom.add("https://media-cdn.tripadvisor.com/media/photo-s/01/fb/17/35/twin-bedroom.jpg")
        listImageDoubleRoom.add("http://cntravelcambodia.com/filelibrary/2492016147467083974567.jpg")
        listImageDoubleRoom.add("https://www.granviakyoto.com/rooms/assets_c/2014/03/std_tw06-thumb-960xauto-33.jpg")
        listImageDoubleRoom.add("http://hotelhennessis.com/wp-content/uploads/2015/12/Hennessis-Hotel-Twin-Room-Setting.jpg")

        listImageFamilyRoom.add("https://www.regalhotel.com/uploads/roh/accommodations/720x475/ROH_RM629-Family-Room-Quadruple.jpg")
        listImageFamilyRoom.add("https://pix10.agoda.net/hotelImages/565/5651/5651_17080313560054867934.jpg?s=1024x768")
        listImageFamilyRoom.add("https://media-cdn.tripadvisor.com/media/photo-s/0b/05/9e/d2/family-quadruple-room.jpg")
        listImageFamilyRoom.add("https://www.regalhotel.com/uploads/rah/promotion/room/720x475/RAH-FamilyQuadruple2.jpg")
    }

    private fun setGuestAllowance() {
        static_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedRoomType: String = static_spinner.getItemAtPosition(position).toString()
                when (selectedRoomType.toLowerCase()) {
                    "single room" -> {
                        checkGuestAllowancePerRoomOnRoomTypeChanged(3)
                        imageSlider.setImageSlider(
                            this@BookHotelActivity,
                            listImageSingleRoom,
                            vgBookHotelImageSlider,
                            bookHotelCircleIndicator
                        )
                    }
                    "double room" -> {
                        checkGuestAllowancePerRoomOnRoomTypeChanged(5)
                        imageSlider.setImageSlider(
                            this@BookHotelActivity,
                            listImageDoubleRoom,
                            vgBookHotelImageSlider,
                            bookHotelCircleIndicator
                        )
                    }
                    "family room" -> {
                        checkGuestAllowancePerRoomOnRoomTypeChanged(7)
                        imageSlider.setImageSlider(
                            this@BookHotelActivity,
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
                            checkGuestAllowancePerRoomOnRoomQuantityChanged(3, s)
                        }
                        "double room" -> {
                            checkGuestAllowancePerRoomOnRoomQuantityChanged(5, s)
                        }
                        "family room" -> {
                            checkGuestAllowancePerRoomOnRoomQuantityChanged(7, s)
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

    fun checkGuestAllowancePerRoomOnRoomQuantityChanged(allowanceQuantity: Int, s: CharSequence) {
        tvNumberOfGuest.text =
            (Integer.parseInt(s.toString()) * allowanceQuantity).toString()
    }

    fun checkGuestAllowancePerRoomOnRoomTypeChanged(allowanceQuantity: Int) {
        if (!etRoomQty.text.toString().isEmpty()) {
            tvNumberOfGuest.text =
                String.format(Locale.US, "%d", allowanceQuantity * Integer.parseInt(etRoomQty.text.toString()))
        } else {
            tvNumberOfGuest.text = allowanceQuantity.toString()
        }
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
                mPrePaidAmount = etDepositeAmount.text.toString().trim()
                if (mRoomQty.isEmpty() || mPrePaidAmount.isEmpty()) {
                    if (mRoomQty.isEmpty()) {
                        etRoomQty.error = "How many room do you want to book with us?"
                    }
                    if (mPrePaidAmount.isEmpty()) {
                        etDepositeAmount.error = "Please input your deposit amount."
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
