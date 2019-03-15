package com.example.homestay.ui.hotel_detail

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.*
import com.example.homestay.R
import com.example.homestay.custom.CustomDialog
import com.example.homestay.custom.ImageSlider
import kotlinx.android.synthetic.main.activity_hotel_detail.*

class HotelDetailActivity : AppCompatActivity(), View.OnClickListener {

    private var listImage: ArrayList<String> = ArrayList()
    private lateinit var hotelName: String
    private lateinit var hotelAddress: String
    private val customDialog = CustomDialog(this@HotelDetailActivity)
    private val hotelDetailMvpPresenter: HotelDetailMvpPresenter = HotelDetailPresenter(this@HotelDetailActivity)

    private val imageSlider: ImageSlider = ImageSlider()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        setContentView(R.layout.activity_hotel_detail)
        setSupportActionBar(toolbarHotelDetail)
        supportActionBar?.title = null

        hotelAddress = intent.getStringExtra("hotel_address")
        hotelName = intent.getStringExtra("hotel_name")

        tvDetailHotelName.text = hotelName
        tvDetailHotelAddress.text = hotelAddress


        listImage.add("https://images.pexels.com/photos/271639/pexels-photo-271639.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940")
        listImage.add("https://images.pexels.com/photos/167533/pexels-photo-167533.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=500&w=500")
        listImage.add("https://images.oyoroomscdn.com/uploads/hotel_image/16810/xlarge/f066ea04eb5916c5.jpg")
        listImage.add("https://images.pexels.com/photos/97083/pexels-photo-97083.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=500&w=500")

        imageSlider.setImageSlider(this, listImage, vgImageSlider, circleIndicator)

        btnViewHotelLocation.setOnClickListener(this)
        btnViewHotelDescription.setOnClickListener(this)
        btnBookHotel.setOnClickListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_hotel_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        hotelDetailMvpPresenter.setActionToSelectedItem(item, R.id.share, R.id.add_to_favorite, hotelName, hotelAddress, listImage)
        return true
    }

    override fun onClick(v: View?) {
        hotelDetailMvpPresenter.setActionToItemClick(v, R.id.btnViewHotelLocation, R.id.btnViewHotelDescription, R.id.btnBookHotel, customDialog, hotelName, hotelAddress)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }
}
