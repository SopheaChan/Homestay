package com.example.homestay.ui.home

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.animation.AnimationUtils
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.homestay.R
import com.example.homestay.adapter.FavoriteHotelAdapter
import com.example.homestay.adapter.HomeAdapter
import com.example.homestay.custom.CustomDialog
import com.example.homestay.custom.DialogDisplayLoadingProgress
import com.example.homestay.model.BookingInfo
import com.example.homestay.model.FavoriteList
import com.example.homestay.model.HotelData
import com.example.homestay.model.User
import com.example.homestay.ui.login.LoginActivity
import com.example.homestay.ui.hotel_detail.HotelDetailActivity
import com.example.homestay.utils.StoreCurrentUserInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView

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
        val hotelData1 = HotelData(
            "https://images.pexels.com/photos/271639/pexels-photo-271639.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940",
            "Lava Hotel", "st. 26DC, New York, USA"
        )
        val hotelData2 = HotelData(
            "https://images.pexels.com/photos/167533/pexels-photo-167533.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=500&w=500",
            "The Park Hotel", "st. 123AY, Athene, Greece"
        )
        val hotelData3 = HotelData(
            "https://images.pexels.com/photos/97083/pexels-photo-97083.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=500&w=500",
            "The Light Hotel", "st. 56NB, London, England"
        )
        val hotelData4 = HotelData(
            "https://images.pexels.com/photos/271639/pexels-photo-271639.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940",
            "The Sky Hotel", "st. 24BC, San Francisco, USA"
        )
        val hotelData5 = HotelData(
            "https://images.pexels.com/photos/167533/pexels-photo-167533.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=500&w=500",
            "The Star Hotel", "st. 11AS, Bali, Indonesia"
        )
        val hotelData6 = HotelData(
            "https://images.pexels.com/photos/97083/pexels-photo-97083.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=500&w=500",
            "The Moon Hotel", "st. 72B0, Rio de Janeiro, Brazil"
        )
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
//        context.startActivity(intent)
    }

    override fun onLoadUser(tvUserName: TextView, tvEmail: TextView, imgProfile: CircleImageView, context: Activity) {
        mDatabaseRef = mFirebaseDb.getReference("profile").child(mUserID)
        mDatabaseRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user: User? = dataSnapshot.getValue(User::class.java)
                tvUserName.text = user?.userBasicInfo?.name ?: "N/A"
                tvEmail.text = user?.userContact?.email ?: "N/A"
                Glide.with(context.baseContext).load(user?.uProfile).into(imgProfile)
                StoreCurrentUserInfo.setUser(user!!)
            }

        })
    }

    override fun onLoadBookingInfo() {
        val bookingRecord: ArrayList<BookingInfo> = arrayListOf()
        mDatabaseRef = mFirebaseDb.getReference("booking").child(mUserID)
        mDatabaseRef.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                /*TODO("not implemented") //To change body of created functions use File | Settings | File Templates.*/
            }

            override fun onDataChange(p0: DataSnapshot) {
                val item = p0.children
                item.forEach{
                    val bookingInfo: BookingInfo ?= it.getValue(BookingInfo::class.java)
                    bookingRecord.add(bookingInfo!!)
                }

                StoreCurrentUserInfo.setBookingInfo(bookingRecord)
            }

        })
    }

    override fun onLoadFavoriteList() {
        val listFavoriteHotel: ArrayList<FavoriteList?> = ArrayList()
        mDatabaseRef = mFirebaseDb.getReference("favorite").child(mUserID)
        mDatabaseRef.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listFavoriteHotel.clear()
                val children = dataSnapshot.children
                children.forEach {
                    val favoriteList = it.getValue(FavoriteList::class.java)
                    listFavoriteHotel.add(favoriteList!!)
                }
                StoreCurrentUserInfo.setFavoriteList(listFavoriteHotel)
            }

        })
    }

    override fun onSignOut(context: Activity, dialog: DialogDisplayLoadingProgress) {
        mAuth.signOut()
        mAuth.addAuthStateListener {
            context.startActivity(Intent(context, LoginActivity::class.java))
            context.finish()
            dialog.getDialog().dismiss()
        }
    }

    override fun onUploadPhoto(context: Activity, newImage: Uri, dialogLoadingProgress: DialogDisplayLoadingProgress) {
        val storageRef: StorageReference = FirebaseStorage.getInstance().getReference("profile").child(mUserID)
        val storageTask = storageRef.putFile(newImage)
            .addOnCompleteListener { taskUpload ->
                if (taskUpload.isSuccessful) {
                    storageRef.downloadUrl
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                val downloadUrl: Uri? = it.result
                                FirebaseDatabase.getInstance().getReference("profile").child(mUserID)
                                    .child("uprofile").setValue(downloadUrl.toString())
                                    .addOnCompleteListener {
                                        dialogLoadingProgress.getDialog().dismiss()
                                    }
                            } else {
                                dialogLoadingProgress.getDialog().dismiss()
                                Toast.makeText(context.baseContext, "Failed to upload image...", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                } else {
                    dialogLoadingProgress.getDialog().dismiss()
                    Toast.makeText(context.baseContext, "Failed to upload image...", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onViewProfilePicture(
        imgImageView: ImageView,
        btnDone: AppCompatButton,
        context: Activity,
        dialog: CustomDialog
    ) {
        val animationIn = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        animationIn.duration = 1000
        imgImageView.animation = animationIn
        btnDone.animation = animationIn

        Glide.with(context.baseContext).load(StoreCurrentUserInfo.getUser().uProfile).into(imgImageView)
        btnDone.setOnClickListener {
            dialog.getDialog().dismiss()
        }
    }

    override fun onViewFavoriteList(context: Activity, layoutID: Int, layoutStyle: Int, customDialog: CustomDialog) {
        customDialog.displayDialog(layoutID, layoutStyle)
        val listFavoriteHotel: ArrayList<FavoriteList?> = StoreCurrentUserInfo.getFavoriteList()
        val adapter = FavoriteHotelAdapter(listFavoriteHotel, context)
        val recyclerView: RecyclerView = customDialog.getDialog().findViewById(R.id.recyclerview_favorite_hotel)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.hasFixedSize()
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
        adapter.onRemoveFromListClicklistener{favoriteList, arrayList ->
            removeFromFavoriteList(favoriteList, arrayList, context, adapter)
        }
    }

    private fun removeFromFavoriteList(
        favoriteHotel: FavoriteList?,
        listFavoriteHotel: ArrayList<FavoriteList?>,
        context: Activity,
        adapter: FavoriteHotelAdapter
    ) {
        val issueDate = favoriteHotel?.issueDate!!
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("favorite").child(mUserID).child(issueDate)
        databaseReference.removeValue().addOnCompleteListener {
                if (it.isSuccessful) {
                    listFavoriteHotel.remove(favoriteHotel)
                    adapter.notifyDataSetChanged()
                    Toast.makeText(context, "Item was removed...", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Failed to remove item from list...", Toast.LENGTH_SHORT).show()
                }
            }
    }
}