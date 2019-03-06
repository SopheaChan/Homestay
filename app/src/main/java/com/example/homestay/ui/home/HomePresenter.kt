package com.example.homestay.ui.home

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.AppCompatButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.homestay.adapter.HomeAdapter
import com.example.homestay.custom.CustomDialog
import com.example.homestay.custom.DialogDisplayLoadingProgress
import com.example.homestay.model.HotelData
import com.example.homestay.model.User
import com.example.homestay.ui.login.LoginActivity
import com.example.homestay.ui.view_detail.HotelDetailActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView
import kotlin.contracts.contract

class HomePresenter : HomeMvpPresenter {
    private var mAuth: FirebaseAuth
    private var mFirebaseDb: FirebaseDatabase
    private lateinit var mDatabaseRef: DatabaseReference
    private var mFirebaseUser: FirebaseUser
    private var mUserID: String

    init {
        this.mAuth = FirebaseAuth.getInstance()
        this.mFirebaseUser = mAuth.currentUser!!
        this.mUserID = mFirebaseUser.uid
        this.mFirebaseDb = FirebaseDatabase.getInstance()
    }

    override fun setDataToList(hotelList: MutableList<HotelData>, adapter: HomeAdapter) {
        val hotelData1 = HotelData("https://images.pexels.com/photos/271639/pexels-photo-271639.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940",
            "Lava Hotel", "st. 26DC, New York, USA")
        val hotelData2 = HotelData("https://images.pexels.com/photos/167533/pexels-photo-167533.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=500&w=500",
            "The Park Hotel", "st. 123AY, Athene, Greece")
        val hotelData3 = HotelData("https://images.pexels.com/photos/97083/pexels-photo-97083.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=500&w=500",
            "The Light Hotel", "st. 56NB, London, England")
        val hotelData4 = HotelData("https://images.pexels.com/photos/271639/pexels-photo-271639.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940",
            "Lava Hotel", "st. 26DC, New York, USA")
        val hotelData5 = HotelData("https://images.pexels.com/photos/167533/pexels-photo-167533.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=500&w=500",
            "The Park Hotel", "st. 123AY, Athene, Greece")
        val hotelData6 = HotelData("https://images.pexels.com/photos/97083/pexels-photo-97083.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=500&w=500",
            "The Light Hotel", "st. 56NB, London, England")
        hotelList.add(hotelData1)
        hotelList.add(hotelData2)
        hotelList.add(hotelData3)
        hotelList.add(hotelData4)
        hotelList.add(hotelData5)
        hotelList.add(hotelData6)
        adapter.notifyDataSetChanged()
    }

    override fun onRecyclerImagesClicked(context: Activity, hotelData: HotelData, imageView: ImageView) {
        val intent = Intent(context.baseContext, HotelDetailActivity::class.java)
        intent.putExtra("hotel_name", hotelData.hotelName)
        intent.putExtra("hotel_address", hotelData.hotelAddress)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            context,
            imageView, imageView.transitionName
        )
        context.startActivity(intent, options.toBundle())
    }

    override fun onLoadUser(tvUserName: TextView, tvEmail: TextView, imgProfile: CircleImageView, context: Activity) {
        mDatabaseRef = mFirebaseDb.getReference("profile").child(mUserID)
        mDatabaseRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user: User ?= dataSnapshot.getValue(User::class.java)
                    tvUserName.text = user?.userBasicInfo?.name ?: "N/A"
                    tvEmail.text = user?.userContact?.email ?: "N/A"
                    Glide.with(context.baseContext).load(user?.uProfile).into(imgProfile)
            }

        })
    }

    override fun onSignOut(context: Activity, dialog: DialogDisplayLoadingProgress) {
        mAuth.signOut()
        mAuth.addAuthStateListener { FirebaseAuth.AuthStateListener {
            context.baseContext.startActivity(Intent(context, LoginActivity::class.java))
            context.finish()
            dialog.getDialog().dismiss()
        } }
    }

    override fun onUploadPhoto(context: Activity, newImage: Uri, dialogLoadingProgress: DialogDisplayLoadingProgress) {
        val storageRef: StorageReference = FirebaseStorage.getInstance().getReference("profile").child(mUserID)
        val storageTask = storageRef.putFile(newImage)
            .addOnCompleteListener{ taskUpload ->
                if (taskUpload.isSuccessful){
                    storageRef.downloadUrl
                        .addOnCompleteListener {
                            if (it.isSuccessful){
                                val downloadUrl: Uri ?= it.result
                                FirebaseDatabase.getInstance().getReference("profile").child(mUserID)
                                    .child("uprofile").setValue(downloadUrl.toString())
                                    .addOnCompleteListener {
                                        dialogLoadingProgress.getDialog().dismiss()
                                    }
                            } else{
                                dialogLoadingProgress.getDialog().dismiss()
                                Toast.makeText(context.baseContext, "Failed to upload image...", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else{
                    dialogLoadingProgress.getDialog().dismiss()
                    Toast.makeText(context.baseContext, "Failed to upload image...", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onViewProfilePicture(imgImageView: ImageView, btnDone: AppCompatButton, context: Activity, dialog: CustomDialog) {
        mDatabaseRef = mFirebaseDb.getReference("profile").child(mUserID)
        mDatabaseRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user: User ?= dataSnapshot.getValue(User::class.java)
                Glide.with(context.baseContext).load(user?.uProfile).into(imgImageView)
                btnDone.setOnClickListener{
                    dialog.getDialog().dismiss()
                }
            }

        })
    }
}