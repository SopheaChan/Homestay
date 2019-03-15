package com.example.homestay.ui.hotel_detail

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.support.annotation.RequiresApi
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.example.homestay.R
import com.example.homestay.custom.CustomDialog
import com.example.homestay.model.FavoriteList
import com.example.homestay.ui.book_hotel.BookHotelActivity
import com.example.homestay.ui.maps.MapsActivity
import com.example.homestay.utils.GetCurrentDateTime
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class HotelDetailPresenter(private val activity: Activity) : HotelDetailMvpPresenter {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun addToFavorite(context: Activity, hotelName: String, hotelAddress: String, imageUrl: String) {
        val firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        val userID: String = firebaseUser!!.uid
        val issueDate: String = GetCurrentDateTime.getAddedToFavoriteListOnDate() ?: "N/A"
        val favoriteList = FavoriteList(hotelName, hotelAddress, issueDate, imageUrl)
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("favorite")
            .child(userID)
            .child(issueDate)
        databaseReference.setValue(favoriteList)
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                Toast.makeText(context.baseContext, "Success...", Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun setActionToItemClick(
        v: View?,
        itemButtonViewHotelLocation: Int,
        itemButtonViewHotelDescription: Int,
        itemButtonBookHotel: Int,
        customDialog: CustomDialog,
        hotelName: String,
        hotelAddress: String
    ) {
        when (v?.id) {
            R.id.btnViewHotelLocation -> {
                activity.startActivity(Intent(activity.baseContext, MapsActivity::class.java))
            }
            R.id.btnViewHotelDescription -> {
                customDialog.displayDialog(R.layout.dialog_view_hotel_description, R.style.DialogTheme)
            }
            R.id.btnBookHotel -> {
                val intent = Intent(activity.baseContext, BookHotelActivity::class.java)
                intent.putExtra("hotel_name", hotelName)
                intent.putExtra("hotel_address", hotelAddress)
                activity.startActivity(intent)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun setActionToSelectedItem(
        item: MenuItem?,
        itemShare: Int,
        itemAddToFavorite: Int,
        hotelName: String,
        hotelAddress: String,
        listImage: ArrayList<String>
    ) {
        when (item?.itemId) {
            R.id.share -> {

            }
            R.id.add_to_favorite -> {
                val title = item.title
                if (title == "Mark as favorite") {
                    item.setIcon(R.drawable.ic_favorite_border_light_pink_24dp)
                    item.title = "Added to favorite"
                    addToFavorite(activity, hotelName, hotelAddress, listImage[0])
                } else if (title == "Added to favorite") {
                    item.setIcon(R.drawable.ic_menu_favorite_white)
                    item.title = "Mark as favorite"
                }
            }
        }
    }
}