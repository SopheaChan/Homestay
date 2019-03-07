package com.example.homestay.ui.view_detail

import android.app.Activity
import android.os.Build
import android.support.annotation.RequiresApi
import android.widget.Toast
import com.example.homestay.model.FavoriteList
import com.example.homestay.utils.GetCurrentDateTime
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class HotelDetailPresenter : HotelDetailMvpPresenter{

    @RequiresApi(Build.VERSION_CODES.N)
    override fun addToFavorite(context: Activity, hotelName: String, hotelAddress: String) {
        val firebaseUser: FirebaseUser ?= FirebaseAuth.getInstance().currentUser
        val userID: String = firebaseUser!!.uid
        val issueDate: String = GetCurrentDateTime.getCurrentTimeUsingDate() ?: "default date"
        val favoriteList = FavoriteList(hotelName, hotelAddress, issueDate)
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("favorite")
            .child(userID)
            .child(issueDate)
        databaseReference.setValue(favoriteList)
        /*databaseReference.child("hotelName").setValue(hotelName)
        databaseReference.child("hotelAddress").setValue(hotelAddress)
        databaseReference.child("time").setValue(GetCurrentDateTime.getCurrentTimeUsingDate())*/
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                Toast.makeText(context.baseContext, "Success...", Toast.LENGTH_SHORT).show()
            }

        })
    }
}