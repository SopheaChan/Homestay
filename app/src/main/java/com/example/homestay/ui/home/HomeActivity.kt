package com.example.homestay.ui.home

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.support.v7.widget.SearchView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import com.example.homestay.R
import com.example.homestay.adapter.HomeAdapter
import com.example.homestay.adapter.HomeAdapter.RecyclerItemsClickedListener
import com.example.homestay.model.HotelData
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*
import android.widget.Toast
import com.example.homestay.custom.DialogDisplayLoadingProgress
import com.example.homestay.custom.DialogMenu
import com.example.homestay.custom.CustomDialog
import com.example.homestay.listener.OnDialogMenuClickListener
import com.example.homestay.ui.login.LoginActivity
import java.io.ByteArrayOutputStream


class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    TextWatcher {

    private lateinit var recyclerView: RecyclerView
    private lateinit var homeAdapter: HomeAdapter
    private lateinit var listHotel: MutableList<HotelData>

    private lateinit var imgUserProfile: ImageView
    private lateinit var tvUserName: TextView
    private lateinit var tvUserEmail: TextView
    private lateinit var dialogMenu: DialogMenu
    private lateinit var searchViewHandler: SearchView

    private val homePresenter: HomeMvpPresenter = HomePresenter()
    private val dialogDisplayLoadingProgress = DialogDisplayLoadingProgress(this@HomeActivity)

    private lateinit var miSearch: MenuItem
    private lateinit var tvTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        listHotel = mutableListOf<HotelData>()

        recyclerView = findViewById(R.id.recycler_view_home_activity)
        recyclerView.hasFixedSize()
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        homeAdapter = HomeAdapter(this, listHotel, recyclerItemsClickedListener)

        recyclerView.adapter = homeAdapter

        dialogDisplayLoadingProgress.displayLoadingProgressTimeDefined("Loading...")
        setDataToAdapter()
        etSearchHotel.addTextChangedListener(this)

    }

    private fun onUserProfileClicked() {
        dialogMenu.displayDialog()
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

            }
            else -> return@OnDialogMenuClickListener
        }
    }

    private fun setDataToAdapter() {
        homePresenter.setDataToList(listHotel, homeAdapter)
    }

    private var recyclerItemsClickedListener = object : RecyclerItemsClickedListener {
        override fun onHotelPictureClickedListener(hotelData: HotelData, imageView: ImageView) {
            homePresenter.onRecyclerImagesClicked(this@HomeActivity, hotelData, imageView)
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
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
                if (getItemTitle == "Search"){
                    tvTitle.visibility = View.GONE
                    etSearchHotel.visibility = View.VISIBLE
                    miSearch.icon = getDrawable(R.drawable.ic_menu_close_search_box)
                    miSearch.title = "Close"
                } else {
                    etSearchHotel.visibility = View.INVISIBLE
                    etSearchHotel.text = null
                    tvTitle.visibility = View.VISIBLE
                    miSearch.icon = getDrawable(R.drawable.ic_menu_search)
                    miSearch.title = "Search"
                }
            }
            R.id.menu_nearby_hotel -> {
                dialogDisplayLoadingProgress.displayLoadingProgressRecursive("Loading nearby hotel...")
            }
            else -> return false
        }
        return false
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_location -> {

            }
            R.id.nav_sign_out -> {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            R.id.nav_about_us -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
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

    fun openGallery() {
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
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (checkStoragePermission()) openCamera()
        } else if (requestCode == REQUEST_EXTERNAL_STORAGE_PERMISSION) openCamera()
        else Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show()
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

    private fun filter(text: String){
        var listHotelFiltered: MutableList<HotelData> = mutableListOf()

        for(hotel: HotelData in listHotel){
            if(hotel.hotelName.toLowerCase().contains(text.toLowerCase())){
                listHotelFiltered.add(hotel)
            }
        }

        homeAdapter.getFilter(listHotelFiltered)

    }
    companion object {
        private val GALLERY = 1
        private val CAMERA = 2
        private val VIEW_PROFILE_PICTURE = 3
        private val REQUEST_CAMERA_PERMISSION = 4
        private val REQUEST_EXTERNAL_STORAGE_PERMISSION = 5
    }
}
