package com.example.homestay.ui.sign_up

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.example.homestay.R
import com.example.homestay.utils.MyObject

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        setContentView(R.layout.activity_sign_up)
        MyObject.setFragmentManager(supportFragmentManager)
        addFragment()
    }

    private fun addFragment() {
        val fragment : Fragment = UserInfoFragment()
        val fragmentManager : FragmentManager = supportFragmentManager
        val fragmentTransaction : FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.layout_fragment_frame, fragment)
        fragmentTransaction.commit()
    }


}
