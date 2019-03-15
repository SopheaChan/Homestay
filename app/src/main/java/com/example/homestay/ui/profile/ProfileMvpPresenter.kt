package com.example.homestay.ui.profile

import android.widget.ImageView
import android.widget.TextView
import de.hdodenhof.circleimageview.CircleImageView

interface ProfileMvpPresenter {
    fun loadUserInfo(profileImage: CircleImageView, tvName: TextView, tvAge: TextView,
                     tvGender: TextView, tvPhone: TextView, tvEmail: TextView,
                     tvAddress: TextView, imageBackground: ImageView
    )
}