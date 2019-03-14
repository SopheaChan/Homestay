package com.example.homestay.ui.home

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.design.widget.NavigationView
import android.support.transition.TransitionSet
import android.support.v4.app.ActivityCompat
import android.support.v4.view.GravityCompat
import android.support.v4.view.animation.LinearOutSlowInInterpolator
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.text.Editable
import android.text.TextWatcher
import android.transition.Transition
import android.transition.TransitionManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.homestay.R
import com.example.homestay.adapter.FavoriteHotelAdapter
import com.example.homestay.adapter.HomeAdapter
import com.example.homestay.custom.CustomDialog
import com.example.homestay.custom.DialogDisplayLoadingProgress
import com.example.homestay.custom.DialogMenu
import com.example.homestay.listener.OnDialogMenuClickListener
import com.example.homestay.model.FavoriteList
import com.example.homestay.model.HotelData
import com.example.homestay.ui.booking_record.BookingRecord
import com.example.homestay.ui.maps.MapsActivity
import com.example.homestay.ui.profile.ProfileActivity
import com.example.homestay.utils.LocationPermission
import com.example.homestay.utils.StoreCurrentUserInfo
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*
import java.io.ByteArrayOutputStream


class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    TextWatcher {

    private lateinit var recyclerView: RecyclerView
    private lateinit var homeAdapter: HomeAdapter
    private lateinit var listHotel: MutableList<HotelData>

    private lateinit var imgUserProfile: CircleImageView
    private lateinit var tvUserName: TextView
    private lateinit var tvUserEmail: TextView
    private lateinit var dialogMenu: DialogMenu
    private lateinit var searchViewHandler: SearchView
    private lateinit var mAuth: FirebaseAuth
    private var backPress: Int = 1

    private lateinit var customDialog: CustomDialog
    private val homePresenter: HomeMvpPresenter = HomePresenter()
    private val dialogDisplayLoadingProgress = DialogDisplayLoadingProgress(this@HomeActivity)

    private lateinit var miSearch: MenuItem
    private lateinit var tvTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        setContentView(com.example.homestay.R.layout.activity_home)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.app_title)

        dialogMenu = DialogMenu(this, dialogMenuCallback)
        etSearchHotel.visibility = View.INVISIBLE

        val navHeader = nav_view.getHeaderView(0)
        imgUserProfile = navHeader.findViewById(R.id.imgUserProfilePicture)
        tvUserName = navHeader.findViewById(R.id.tvUserName)
        tvUserEmail = navHeader.findViewById(R.id.tvUserEmail)
        tvTitle = findViewById(R.id.tvAppTitle)
        searchViewHandler = SearchView(this)

        imgUserProfile.setOnClickListener {
            onUserProfileClicked()
        }

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        listHotel = mutableListOf()

        recyclerView = findViewById(R.id.recycler_view_home_activity)
        recyclerView.hasFixedSize()
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        homeAdapter = HomeAdapter(this, listHotel)
        homeAdapter.setOnItemClickListener { hotelData, imageView ->
            homePresenter.onRecyclerImagesClicked(this@HomeActivity, hotelData, imageView)
        }

        recyclerView.adapter = homeAdapter

        dialogDisplayLoadingProgress.displayLoadingProgressTimeDefined("Loading...")
        setDataToAdapter()
        etSearchHotel.addTextChangedListener(this)
        homePresenter.onLoadUser(tvUserName, tvUserEmail, imgUserProfile, this)
        customDialog = CustomDialog(this@HomeActivity)
        homePresenter.onLoadFavoriteList()
        homePresenter.onLoadBookingInfo()
    }

    private fun onUserProfileClicked() {
        dialogMenu.displayDialog()
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        when {
            drawerLayout.isDrawerVisible(GravityCompat.START) -> {
                drawerLayout.closeDrawer(GravityCompat.START)
                backPress = 0
            }
            backPress < 1 -> {
                Toast.makeText(this@HomeActivity, "Press back again to exit!", Toast.LENGTH_SHORT).show()
                backPress++
            }
            else -> finish()
        }
    }

    private val dialogMenuCallback = OnDialogMenuClickListener { result, view, dialog ->
        when (result) {
            GALLERY -> {
                openGallery()
                dialog.dismiss()
            }
            CAMERA -> {
                if (checkCameraPermission()) {
                    if (checkStoragePermission()) {
                        openCamera()
                        dialog.dismiss()
                    }
                }
            }
            VIEW_PROFILE_PICTURE -> {
                val customDialog = CustomDialog(this)
                customDialog.displayDialog(R.layout.layout_view_profile_picture, R.style.DialogBookHotelTheme)
                val imgProfile = customDialog.getDialog().findViewById<ImageView>(R.id.imgImageView)
                val btnDone = customDialog.getDialog().findViewById<AppCompatButton>(R.id.btnDone)

                homePresenter.onViewProfilePicture(imgProfile, btnDone, this, customDialog)

            }
            else -> return@OnDialogMenuClickListener
        }
    }

    private fun setDataToAdapter() {
        homePresenter.setDataToList(listHotel, homeAdapter)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home, menu)
        miSearch = menu.findItem(R.id.menu_search)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.khmer_language -> {
                item.isChecked = true
            }
            R.id.english_language -> {
                item.isChecked = true
            }
            R.id.menu_search -> {
                val getItemTitle = miSearch.title
                if (getItemTitle == "Search") {
                    setAnimationToTitle(tvTitle)
                    tvTitle.visibility = View.GONE
//                    etSearchHotel.visibility = View.VISIBLE
                    miSearch.icon = getDrawable(R.drawable.ic_menu_close_search_box)
                    miSearch.title = "Close"
                } else {
                    setAnimationToEtSearch(etSearchHotel)
                    etSearchHotel.visibility = View.INVISIBLE
                    etSearchHotel.text = null
//                    tvTitle.visibility = View.VISIBLE
                    miSearch.icon = getDrawable(R.drawable.ic_menu_search)
                    miSearch.title = "Search"
                }
            }
            R.id.menu_nearby_hotel -> {
                if (LocationPermission.checkLocationPermission(this)){
                    val intent = Intent(this@HomeActivity, MapsActivity::class.java)
                    startActivity(intent)
                }
            }
            else -> return false
        }
        return false
    }

    private fun setAnimationToTitle(tvTitle: TextView) {
        val animation = AnimationUtils.loadAnimation(this@HomeActivity, R.anim.fade_out)
        animation.duration = 700
        tvTitle.animation = animation
        animation.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                setAnimationIn(etSearchHotel)
//                setAnimationToTitle(tvTitle)
            }

            override fun onAnimationStart(animation: Animation?) {

            }

        })
    }

    private fun setAnimationIn(etSearch: EditText){
        val animation = AnimationUtils.loadAnimation(this@HomeActivity, R.anim.fade_in)
        animation.duration = 700
        etSearch.animation = animation
        animation.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                etSearch.visibility = View.VISIBLE
            }

            override fun onAnimationStart(animation: Animation?) {
            }

        })
    }

    private fun setAnimationToEtSearch(etSearch: EditText) {
        val animation = AnimationUtils.loadAnimation(this@HomeActivity, R.anim.fade_out)
        animation.duration = 700
        etSearch.animation = animation
        animation.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                setTitleAnimationIn(tvTitle)
            }

            override fun onAnimationStart(animation: Animation?) {

            }

        })
    }

    private fun setTitleAnimationIn(textTitle: TextView){
        val animation = AnimationUtils.loadAnimation(this@HomeActivity, R.anim.fade_in)
        animation.duration = 700
        textTitle.animation = animation
        animation.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                textTitle.visibility = View.VISIBLE
            }

            override fun onAnimationStart(animation: Animation?) {
            }

        })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_sign_out -> {
                dialogDisplayLoadingProgress.displayLoadingProgressRecursive("Signing out...")
                homePresenter.onSignOut(this, dialogDisplayLoadingProgress)
            }
            R.id.nav_about_us -> {

            }

            R.id.nav_favorite -> {
                openFavoriteListDialog()
            }
            R.id.nav_profile -> {
                startActivity(Intent(this@HomeActivity, ProfileActivity::class.java))
            }
            R.id.nav_booking -> {
                startActivity(Intent(this, BookingRecord::class.java))
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun openFavoriteListDialog() {
        homePresenter.onViewFavoriteList(this@HomeActivity, R.layout.dialog_favorite_list, R.style.DialogFavoriteHotelTheme, customDialog)
    }

    private fun checkCameraPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@HomeActivity,
                arrayOf(android.Manifest.permission.CAMERA),
                REQUEST_CAMERA_PERMISSION
            )
            return false
        }

        return true
    }

    private fun checkStoragePermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) !=
            PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ), REQUEST_EXTERNAL_STORAGE_PERMISSION
            )
            return false
        }
        return true
    }

    private fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, CAMERA)
        }
    }

    private fun openGallery() {
        val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(intent, GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                GALLERY -> {
                    val imageUri = data!!.data
                    imgUserProfile.setImageURI(imageUri)
                    dialogMenu.dialog.dismiss()
                    homePresenter.onUploadPhoto(this, imageUri, dialogDisplayLoadingProgress)
                    dialogDisplayLoadingProgress.displayLoadingProgressRecursive("Uploading...")
                }
                CAMERA -> {
                    val extras = data!!.extras
                    val imageBitmap = extras!!.get("data") as Bitmap

                    val os = ByteArrayOutputStream()
                    imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, os)
                    val path =
                        MediaStore.Images.Media.insertImage(contentResolver, imageBitmap, "profile_picture", null)
                    imgUserProfile.setImageURI(Uri.parse(path))
                    dialogMenu.dialog.dismiss()
                    homePresenter.onUploadPhoto(this, Uri.parse(path), dialogDisplayLoadingProgress)
                    dialogDisplayLoadingProgress.displayLoadingProgressRecursive("Uploading...")
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode){
            REQUEST_CAMERA_PERMISSION -> {
                if (checkStoragePermission()) openCamera()
            }
            REQUEST_EXTERNAL_STORAGE_PERMISSION -> {
                openCamera()
            }
            REQUEST_LOCATION -> {
                if (LocationPermission.checkLocationPermission(this)) {
                    val intent = Intent(this@HomeActivity, MapsActivity::class.java)
                    startActivity(intent)
                }
            }
            else -> Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun afterTextChanged(s: Editable?) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        filter(s.toString())
    }

    private fun filter(text: String) {
        var listHotelFiltered: MutableList<HotelData> = mutableListOf()

        for (hotel: HotelData in listHotel) {
            if (hotel.hotelName.toLowerCase().contains(text.toLowerCase())) {
                listHotelFiltered.add(hotel)
            }
        }

        homeAdapter.getFilter(listHotelFiltered)

    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onPause() {
        super.onPause()
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
