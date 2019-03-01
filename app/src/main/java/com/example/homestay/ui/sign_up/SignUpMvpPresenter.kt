package com.example.homestay.ui.sign_up

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

interface SignUpMvpPresenter {
    fun onButtonNextListener(fragmentManager: FragmentManager, fragment: Fragment)
    fun onButtonBackListener(fragmentManager: FragmentManager, fragment: Fragment)
}