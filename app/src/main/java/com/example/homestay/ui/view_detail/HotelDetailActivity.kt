package com.example.homestay.ui.view_detail

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import com.example.homestay.R
import com.example.homestay.adapter.ImageSlideshowAdapter
import com.example.homestay.custom.CustomDialog
import com.example.homestay.ui.maps.MapsActivity
import kotlinx.android.synthetic.main.activity_hotel_detail.*
import java.util.*
import kotlin.collections.ArrayList

class HotelDetailActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mAdapter: ImageSlideshowAdapter
    private lateinit var mHandler: Handler
    private lateinit var mRunnable: Runnable
    private lateinit var mTimer: Timer

    private var listImage: ArrayList<String> = ArrayList()
    private lateinit var hotelName: String
    private lateinit var hotelAddress: String
    private val customDialog = CustomDialog(this@HotelDetailActivity)
    private val hotelDetailMvpPresenter: HotelDetailMvpPresenter = HotelDetailPresenter()


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
        mAdapter = ImageSlideshowAdapter(this, listImage)
        vgImageSlider.adapter = mAdapter
        circleIndicator.setViewPager(vgImageSlider)

        mHandler = Handler()
        mRunnable = Runnable {
            var i = vgImageSlider.currentItem

            if (i == mAdapter.count - 1) {

                i = 0
                vgImageSlider.setCurrentItem(i, true)

            } else {

                i++
                vgImageSlider.setCurrentItem(i, true)
            }
        }

        mTimer = Timer()
        mTimer.schedule(object : TimerTask() {
            override fun run() {

                mHandler.post(mRunnable)
            }
        }, 4000, 4000)
        mAdapter.notifyDataSetChanged()
        btnViewHotelLocation.setOnClickListener(this)
        btnViewHotelDescription.setOnClickListener(this)
        btnBookHotel.setOnClickListener(this)
//        setAnimationToView()
    }

    //set animation to tvDetailRoomPrice
   /* private fun setAnimationToView() {
        val animation = AnimationUtils.loadAnimation(this@HotelDetailActivity, R.anim.view_anim)
        tvDetailRoomPrice.animation = animation
        animation.duration = 5000
        animation.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                setAnimationToView()
            }

            override fun onAnimationStart(animation: Animation?) {

            }

        })
    }*/

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_hotel_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.share -> {

            }
            R.id.add_to_favorite -> {
                val title = item.title
                if (title == "Mark as favorite") {
                    item.setIcon(R.drawable.ic_favorite_border_light_pink_24dp)
                    item.title = "Added to favorite"
                    hotelDetailMvpPresenter.addToFavorite(this, hotelName, hotelAddress)
                } else if (title == "Added to favorite") {
                    item.setIcon(R.drawable.ic_menu_favorite_white)
                    item.title = "Mark as favorite"
                }
            }
            else -> return false
        }
        return true
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnViewHotelLocation -> {
                startActivity(Intent(this, MapsActivity::class.java))
            }
            R.id.btnViewHotelDescription -> {
                customDialog.displayDialog(R.layout.dialog_view_hotel_description, R.style.DialogTheme)
            }
            R.id.btnBookHotel -> {
                customDialog.displayDialog(R.layout.dialog_book_hotel, R.style.DialogBookHotelTheme)
            }
        }
    }
}
