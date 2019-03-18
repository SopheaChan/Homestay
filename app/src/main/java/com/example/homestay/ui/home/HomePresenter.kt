package com.example.homestay.ui.home

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import com.bumptech.glide.Glide
import com.example.homestay.R
import com.example.homestay.adapter.FavoriteHotelAdapter
import com.example.homestay.adapter.HomeAdapter
import com.example.homestay.custom.CustomDialog
import com.example.homestay.custom.DialogDisplayLoadingProgress
import com.example.homestay.custom.DialogMenu
import com.example.homestay.model.BookingInfo
import com.example.homestay.model.FavoriteList
import com.example.homestay.model.HotelData
import com.example.homestay.model.User
import com.example.homestay.ui.booking_record.BookingRecord
import com.example.homestay.ui.login.LoginActivity
import com.example.homestay.ui.hotel_detail.HotelDetailActivity
import com.example.homestay.ui.maps.MapsActivity
import com.example.homestay.ui.profile.ProfileActivity
import com.example.homestay.utils.LocationPermission
import com.example.homestay.utils.StoreCurrentUserInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*
import java.io.ByteArrayOutputStream

class HomePresenter(private val context: Activity) : HomeMvpPresenter {
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

    override fun onRecyclerImagesClicked(hotelData: HotelData, imageView: ImageView) {
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

    override fun onLoadUser(tvUserName: TextView, tvEmail: TextView, imgProfile: CircleImageView) {
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
        mDatabaseRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                /*TODO("not implemented") //To change body of created functions use File | Settings | File Templates.*/
            }

            override fun onDataChange(p0: DataSnapshot) {
                bookingRecord.clear()
                val item = p0.children
                item.forEach {
                    val bookingInfo: BookingInfo? = it.getValue(BookingInfo::class.java)
                    bookingRecord.add(bookingInfo!!)
                }

                StoreCurrentUserInfo.setBookingInfo(bookingRecord)
            }

        })
    }

    override fun onLoadFavoriteList() {
        val listFavoriteHotel: ArrayList<FavoriteList?> = ArrayList()
        mDatabaseRef = mFirebaseDb.getReference("favorite").child(mUserID)
        mDatabaseRef.addValueEventListener(object : ValueEventListener {
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

    override fun onSignOut(dialog: DialogDisplayLoadingProgress) {
        mAuth.signOut()
        mAuth.addAuthStateListener {
            context.startActivity(Intent(context.baseContext, LoginActivity::class.java))
            context.finish()
            dialog.getDialog().dismiss()
        }
    }

    private fun onUploadPhoto(newImage: Uri, dialogLoadingProgress: DialogDisplayLoadingProgress) {
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

    private fun onViewProfilePicture(
        imgImageView: ImageView,
        btnDone: AppCompatButton,
        dialog: CustomDialog,
        animation: Int
    ) {
        val animationIn = AnimationUtils.loadAnimation(context.baseContext, animation)
        animationIn.duration = 1000
        imgImageView.animation = animationIn
        btnDone.animation = animationIn

        Glide.with(context.baseContext).load(StoreCurrentUserInfo.getUser().uProfile).into(imgImageView)
        btnDone.setOnClickListener {
            dialog.getDialog().dismiss()
        }
    }

    override fun onViewFavoriteList(layoutID: Int, layoutStyle: Int, customDialog: CustomDialog) {
        customDialog.displayDialog(layoutID, layoutStyle)
        val listFavoriteHotel: ArrayList<FavoriteList?> = StoreCurrentUserInfo.getFavoriteList()
        val adapter = FavoriteHotelAdapter(listFavoriteHotel, context)
        val recyclerView: RecyclerView = customDialog.getDialog().findViewById(R.id.recyclerview_favorite_hotel)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.hasFixedSize()
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
        adapter.onRemoveFromListClicklistener { favoriteList, arrayList ->
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
        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("favorite").child(mUserID).child(issueDate)
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

    override fun onOpenGallery() {
        val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        context.startActivityForResult(intent, GALLERY)
    }

    override fun onListFilter(inputText: String, listHotel: MutableList<HotelData>, homeAdapter: HomeAdapter) {
        var listHotelFiltered: MutableList<HotelData> = mutableListOf()

        for (hotel: HotelData in listHotel) {
            if (hotel.hotelName.toLowerCase().contains(inputText.toLowerCase())) {
                listHotelFiltered.add(hotel)
            }
        }

        homeAdapter.getFilter(listHotelFiltered)
    }

    fun onOpenCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(context.packageManager) != null) {
            context.startActivityForResult(takePictureIntent, CAMERA)
        }
    }

    override fun onPermissionResult(requestCode: Int) {
        when (requestCode) {
            REQUEST_CAMERA_PERMISSION -> {
                if (checkStoragePermission()) onOpenCamera()
//                    openCamera()
            }
            REQUEST_EXTERNAL_STORAGE_PERMISSION -> {
                onOpenCamera()
//                openCamera()
            }
            REQUEST_LOCATION -> {
                if (LocationPermission.checkLocationPermission(context)) {
                    val intent = Intent(context.baseContext, MapsActivity::class.java)
                    context.startActivity(intent)
                }
            }
            else -> Toast.makeText(context.baseContext, "Permission denied!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkStoragePermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                context.baseContext,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) !=
            PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                context.baseContext,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context, arrayOf(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ), REQUEST_EXTERNAL_STORAGE_PERMISSION
            )
            return false
        }
        return true
    }

    override fun getActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        imgUserProfile: CircleImageView,
        dialogMenu: DialogMenu,
        dialogDisplayLoadingProgress: DialogDisplayLoadingProgress
    ) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                GALLERY -> {
                    val imageUri = data!!.data
                    imgUserProfile.setImageURI(imageUri)
                    dialogMenu.dialog.dismiss()
                    onUploadPhoto(imageUri, dialogDisplayLoadingProgress)
                    dialogDisplayLoadingProgress.displayLoadingProgressRecursive("Uploading...")
                }
                CAMERA -> {
                    val extras = data!!.extras
                    val imageBitmap = extras!!.get("data") as Bitmap

                    val os = ByteArrayOutputStream()
                    imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, os)
                    val path =
                        MediaStore.Images.Media.insertImage(
                            context.contentResolver,
                            imageBitmap,
                            "profile_picture",
                            null
                        )
                    imgUserProfile.setImageURI(Uri.parse(path))
                    dialogMenu.dialog.dismiss()
                    onUploadPhoto(Uri.parse(path), dialogDisplayLoadingProgress)
                    dialogDisplayLoadingProgress.displayLoadingProgressRecursive("Uploading...")
                }
            }
        }
    }

    override fun onDialogMenuCallback(
        result: Int,
        dialog: Dialog,
        dialogLayout: Int,
        dialogStyle: Int,
        animation: Int
    ) {
        when (result) {
            GALLERY -> {
//                openGallery()
                onOpenGallery()
                dialog.dismiss()
            }
            CAMERA -> {
                if (checkCameraPermission()) {
                    if (checkStoragePermission()) {
//                        openCamera()
                        onOpenCamera()
                        dialog.dismiss()
                    }
                }
            }
            VIEW_PROFILE_PICTURE -> {
                val customDialog = CustomDialog(context)
                customDialog.displayDialog(dialogLayout, dialogStyle)
                val imgProfile = customDialog.getDialog().findViewById<ImageView>(R.id.imgImageView)
                val btnDone = customDialog.getDialog().findViewById<AppCompatButton>(R.id.btnDone)

                onViewProfilePicture(imgProfile, btnDone, customDialog, animation)

            }
            else -> return
        }
    }

    private fun checkCameraPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                context.baseContext,
                android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context,
                arrayOf(android.Manifest.permission.CAMERA),
                REQUEST_CAMERA_PERMISSION
            )
            return false
        }

        return true
    }

    override fun setActionToOptionsItem(
        menuItem: MenuItem,
        itemKhmerLanguage: Int,
        itemEnglishLanguage: Int,
        itemSearch: Int,
        itemNearbyHotel: Int,
        tvTitle: TextView,
        etSearch: EditText,
        miSearch: MenuItem
    ) {
        when (menuItem.itemId) {
            R.id.khmer_language -> {
                menuItem.isChecked = true
            }
            R.id.english_language -> {
                menuItem.isChecked = true
            }
            R.id.menu_search -> {
                val getItemTitle = miSearch.title
                if (getItemTitle == "Search") {
                    setAnimationToTitle(tvTitle, etSearch)
                    tvTitle.visibility = View.GONE
//                    etSearchHotel.visibility = View.VISIBLE
                    miSearch.icon = context.getDrawable(R.drawable.ic_menu_close_search_box)
                    miSearch.title = "Close"
                } else {
                    setAnimationToEtSearch(etSearch, tvTitle)
                    etSearch.visibility = View.INVISIBLE
                    etSearch.text = null
//                    tvTitle.visibility = View.VISIBLE
                    miSearch.icon = context.getDrawable(R.drawable.ic_menu_search)
                    miSearch.title = "Search"
                }
            }
            R.id.menu_nearby_hotel -> {
                if (LocationPermission.checkLocationPermission(context)) {
                    val intent = Intent(context.baseContext, MapsActivity::class.java)
                    context.startActivity(intent)
                }
            }
        }
    }

    private fun setAnimationToTitle(tvTitle: TextView, etSearch: EditText) {
        val animation = AnimationUtils.loadAnimation(context.baseContext, R.anim.fade_out)
        animation.duration = 700
        tvTitle.animation = animation
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                setAnimationIn(etSearch)
//                setAnimationToTitle(tvTitle)
            }

            override fun onAnimationStart(animation: Animation?) {

            }

        })
    }

    private fun setAnimationIn(etSearch: EditText) {
        val animation = AnimationUtils.loadAnimation(context.baseContext, R.anim.fade_in)
        animation.duration = 700
        etSearch.animation = animation
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                etSearch.visibility = View.VISIBLE
            }

            override fun onAnimationStart(animation: Animation?) {
            }

        })
    }

    private fun setAnimationToEtSearch(etSearch: EditText, tvTitle: TextView) {
        val animation = AnimationUtils.loadAnimation(context.baseContext, R.anim.fade_out)
        animation.duration = 700
        etSearch.animation = animation
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                setTitleAnimationIn(tvTitle)
            }

            override fun onAnimationStart(animation: Animation?) {

            }

        })
    }

    private fun setTitleAnimationIn(textTitle: TextView) {
        val animation = AnimationUtils.loadAnimation(context.baseContext, R.anim.fade_in)
        animation.duration = 700
        textTitle.animation = animation
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                textTitle.visibility = View.VISIBLE
            }

            override fun onAnimationStart(animation: Animation?) {
            }

        })
    }

    override fun setActionToNavigationItem(
        item: MenuItem,
        itemSignOut: Int,
        itemAboutUs: Int,
        itemFavorite: Int,
        itemProfile: Int,
        itemBooking: Int,
        drawer_layout: DrawerLayout,
        dialogDisplayLoadingProgress: DialogDisplayLoadingProgress,
        customDialog: CustomDialog
    ) {
        when (item.itemId) {
            R.id.nav_sign_out -> {
                dialogDisplayLoadingProgress.displayLoadingProgressRecursive("Signing out...")
                onSignOut(dialogDisplayLoadingProgress)
            }
            R.id.nav_about_us -> {

            }

            R.id.nav_favorite -> {
                onViewFavoriteList(R.layout.dialog_favorite_list, R.style.DialogFavoriteHotelTheme, customDialog)
            }
            R.id.nav_profile -> {
                context.startActivity(Intent(context.baseContext, ProfileActivity::class.java))
            }
            R.id.nav_booking -> {
                context.startActivity(Intent(context.baseContext, BookingRecord::class.java))
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
    }

    companion object {
        private const val GALLERY = 1
        private const val CAMERA = 2
        private const val VIEW_PROFILE_PICTURE = 3
        private const val REQUEST_CAMERA_PERMISSION = 4
        private const val REQUEST_EXTERNAL_STORAGE_PERMISSION = 5
        private const val REQUEST_LOCATION = 6
    }
}