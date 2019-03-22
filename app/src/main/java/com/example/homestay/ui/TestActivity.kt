package com.example.homestay.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.ViewPager
import com.example.homestay.R

abstract class BaseViewPagerActivity : BaseActivity() {

    abstract fun initViewPager() : ViewPager


}
