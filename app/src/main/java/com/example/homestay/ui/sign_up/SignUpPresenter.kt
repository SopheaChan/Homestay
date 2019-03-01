package com.example.homestay.ui.sign_up

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import com.example.homestay.R

class SignUpPresenter : SignUpMvpPresenter{

    override fun onButtonNextListener(fragmentManager: FragmentManager, fragment: Fragment) {
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.layout_fragment_frame, fragment)
            fragmentTransaction.commitAllowingStateLoss()
    }

    override fun onButtonBackListener(fragmentManager: FragmentManager, fragment: Fragment) {
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.layout_fragment_frame, fragment)
        fragmentTransaction.commitAllowingStateLoss()
    }


}