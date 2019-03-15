package com.example.homestay.ui.profile

import android.app.Activity
import android.graphics.BlurMaskFilter
import android.view.animation.Transformation
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.example.homestay.model.User
import com.example.homestay.model.UserBasicInfo
import com.example.homestay.model.UserContact
import com.example.homestay.utils.StoreCurrentUserInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView

class ProfilePresenter(private val context: Activity) : ProfileMvpPresenter {

    var firebaseAut: FirebaseAuth
    var firebaseUser: FirebaseUser
    var firebaseStorage: FirebaseStorage
    var databaseRef: DatabaseReference

    init {
        firebaseAut = FirebaseAuth.getInstance()
        firebaseUser = firebaseAut.currentUser!!
        firebaseStorage = FirebaseStorage.getInstance()
        databaseRef = FirebaseDatabase.getInstance().getReference("profile")
    }

    override fun loadUserInfo(
        profileImage: CircleImageView, tvName: TextView, tvAge: TextView,
        tvGender: TextView, tvPhone: TextView, tvEmail: TextView,
        tvAddress: TextView, imageBackground: ImageView) {

            val user: User? = StoreCurrentUserInfo.getUser()
                val userProfile = user?.uProfile
                val userInfo: UserBasicInfo? = user?.userBasicInfo
                val userContact: UserContact? = user?.userContact
                Glide.with(context.baseContext).load(userProfile).into(profileImage)
                Glide.with(context.baseContext).load(userProfile).override(30, 30)  //just set override like this
                    .into(imageBackground)
                tvName.text = userInfo?.name
                tvAge.text = userInfo?.age
                tvGender.text = userInfo?.sex
                tvPhone.text = userContact?.phone
                tvEmail.text = userContact?.email
                tvAddress.text = userInfo?.address
    }
}