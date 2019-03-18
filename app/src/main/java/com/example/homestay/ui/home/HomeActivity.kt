package com.example.homestay.ui.home

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.AbsListView
import android.widget.TextView
import android.widget.Toast
import com.example.homestay.R
import com.example.homestay.adapter.HomeAdapter
import com.example.homestay.custom.CustomDialog
import com.example.homestay.custom.DialogDisplayLoadingProgress
import com.example.homestay.custom.DialogMenu
import com.example.homestay.listener.OnDialogMenuClickListener
import com.example.homestay.model.HotelData
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*


class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    TextWatcher {

    private lateinit var recyclerView: RecyclerView
//    private lateinit var recyclerView: com.mlsdev.animatedrv.AnimatedRecyclerView
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
    private val homePresenter: HomeMvpPresenter = HomePresenter(this@HomeActivity)
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
        val animation: LayoutAnimationController = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation_right_to_left)
        recyclerView.layoutAnimation = animation
        recyclerView.adapter?.notifyDataSetChanged()
        recyclerView.scheduleLayoutAnimation()

        homeAdapter = HomeAdapter(this, listHotel)
        homeAdapter.setOnItemClickListener { hotelData, imageView ->
            homePresenter.onRecyclerImagesClicked(hotelData, imageView)
        }

        recyclerView.adapter = homeAdapter

        dialogDisplayLoadingProgress.displayLoadingProgressTimeDefined("Loading...")
        setDataToAdapter()
        etSearchHotel.addTextChangedListener(this)
        homePresenter.onLoadUser(tvUserName, tvUserEmail, imgUserProfile)
        customDialog = CustomDialog(this@HomeActivity)
        homePresenter.onLoadFavoriteList()
        homePresenter.onLoadBookingInfo()

        /*recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val mAnimation: LayoutAnimationController = AnimationUtils.loadLayoutAnimation(this@HomeActivity, R.anim.layout_animation_right_to_left)
                recyclerView.layoutAnimation = mAnimation
//                Toast.makeText(this@HomeActivity, dx.toString() + dy.toString(), Toast.LENGTH_SHORT).show()
                *//*recyclerView.adapter?.notifyDataSetChanged()
                recyclerView.scheduleLayoutAnimation()*//*

            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
        })*/

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
        homePresenter.onDialogMenuCallback(
            result,
            dialog,
            R.layout.layout_view_profile_picture,
            R.style.DialogBookHotelTheme,
            R.anim.fade_in
        )
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
        homePresenter.setActionToOptionsItem(
            item,
            R.id.khmer_language,
            R.id.english_language,
            R.id.menu_search,
            R.id.menu_nearby_hotel,
            tvTitle,
            etSearchHotel,
            miSearch
        )
         return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        homePresenter.setActionToNavigationItem(
            item,
            R.id.nav_sign_out,
            R.id.nav_about_us,
            R.id.nav_favorite,
            R.id.nav_profile,
            R.id.nav_booking,
            drawer_layout,
            dialogDisplayLoadingProgress,
            customDialog
        )
         return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        homePresenter.getActivityResult(
            requestCode,
            resultCode,
            data,
            imgUserProfile,
            dialogMenu,
            dialogDisplayLoadingProgress
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        homePresenter.onPermissionResult(requestCode)
    }

    override fun afterTextChanged(s: Editable?) {

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        homePresenter.onListFilter(s.toString(), listHotel, homeAdapter)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onPause() {
        super.onPause()
    }
}
