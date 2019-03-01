package com.example.homestay.utils

import android.content.Context
import android.support.v4.app.FragmentManager

object MyObject {
    private lateinit var fragmentManager: FragmentManager

    fun setFragmentManager(fragmentManager: FragmentManager){
        this.fragmentManager = fragmentManager
    }
    fun getFragmentManager():FragmentManager{
        return this.fragmentManager
    }
}