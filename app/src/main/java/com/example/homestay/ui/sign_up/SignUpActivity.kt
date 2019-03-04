package com.example.homestay.ui.sign_up

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import android.widget.Toast
import com.example.homestay.R
import com.example.homestay.utils.MyObject
import com.example.homestay.utils.RequestCode
import java.io.Serializable

class SignUpActivity : AppCompatActivity(), SignUpMvpView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        setContentView(R.layout.activity_sign_up)
        MyObject.setFragmentManager(supportFragmentManager)
        addFragment()
    }

    private fun addFragment() {
        val fragment : Fragment = UserInfoFragment()
        val fragmentManager : FragmentManager = supportFragmentManager
        val fragmentTransaction : FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.layout_fragment_frame, fragment)
        fragmentTransaction.commit()
    }

    override fun browseImage() {

    }

    fun chhoseFromGalery(){
        val intent = Intent(Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, RequestCode.GALLLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RequestCode.GALLLERY && resultCode == Activity.RESULT_OK){
            val imageUri = data!!.data
        }
    }
}
