package com.example.homestay.ui.profile

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.example.homestay.R
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    val profileMvpPresenter: ProfileMvpPresenter = ProfilePresenter(this@ProfileActivity)
    lateinit var toolbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        window.statusBarColor = resources.getColor(R.color.colorPrimary)
        setContentView(R.layout.activity_profile)

        toolbar = findViewById(R.id.toolbar_profile_activity)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Profile"
        loadUserData()
    }

    private fun loadUserData() {
        profileMvpPresenter.loadUserInfo(imgProfileImage, tvProfileName, tvProfileAge, tvProfileGender, tvProfilePhoneNum,
            tvProfileEmail, tvUserAddress, imgImageBackground)
    }
}
