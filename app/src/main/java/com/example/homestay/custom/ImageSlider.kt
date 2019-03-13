package com.example.homestay.custom

import android.app.Activity
import android.os.Handler
import android.support.v4.view.ViewPager
import com.example.homestay.adapter.ImageSlideshowAdapter
import me.relex.circleindicator.CircleIndicator
import java.util.*

class ImageSlider {
    private lateinit var mAdapter: ImageSlideshowAdapter
    private lateinit var mHandler: Handler
    private lateinit var mRunnable: Runnable
    private lateinit var mTimer: Timer

    fun setImageSlider(
        context: Activity,
        listImage: ArrayList<String>,
        viewPager: ViewPager,
        circleIndicator: CircleIndicator) {
        mAdapter = ImageSlideshowAdapter(context.baseContext, listImage)
        viewPager.adapter = mAdapter
        circleIndicator.setViewPager(viewPager)

        mHandler = Handler()
        mRunnable = Runnable {
            var i = viewPager.currentItem

            if (i == mAdapter.count - 1) {

                i = 0
                viewPager.setCurrentItem(i, true)

            } else {

                i++
                viewPager.setCurrentItem(i, true)
            }
        }

        mTimer = Timer()
        mTimer.schedule(object : TimerTask() {
            override fun run() {

                mHandler.post(mRunnable)
            }
        }, 7000, 7000)
        mAdapter.notifyDataSetChanged()
    }
}