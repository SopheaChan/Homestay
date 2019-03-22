package com.example.homestay.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentView())

        initView()
    }

    abstract fun getContentView() : Int

    protected open fun initView(){
        Log.d("asdfsadf", "asdfsadf")
    }
}
