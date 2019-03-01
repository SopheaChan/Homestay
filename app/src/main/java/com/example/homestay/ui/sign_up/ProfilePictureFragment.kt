package com.example.homestay.ui.sign_up

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.AppCompatButton
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.homestay.R
import com.example.homestay.utils.MyObject

class ProfilePictureFragment : Fragment(), View.OnClickListener {

    private val signUpMvpPresenter = SignUpPresenter()
    private lateinit var btnBack: FloatingActionButton
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.layout_sign_up_profile_picture, container, false)
        val btnBrowseImage = view.findViewById<AppCompatButton>(R.id.btnBrowse)
        btnBack = view.findViewById(R.id.btnBack2)
        btnBrowseImage.setOnClickListener(this)
        btnBack.setOnClickListener(this)
        return view
    }

    override fun onClick(v: View?) {
        when (v?.id){
            R.id.btnBrowse -> {

            }
            R.id.btnBack2 -> {
                val fragment = ContactInfoFragment()
                signUpMvpPresenter.onButtonBackListener(MyObject.getFragmentManager(), fragment)
            }
        }
    }




}