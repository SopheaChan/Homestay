package com.example.homestay.custom

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import com.example.homestay.R
import com.example.homestay.listener.OnDialogMenuClickListener

class DialogMenu(private val context: Context, private val callback: OnDialogMenuClickListener) : View.OnClickListener {
    var dialog: Dialog

    init {
        this.dialog = Dialog(this.context, R.style.DialogTheme)
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog.setContentView(R.layout.dialog_choose_profile_photo)
//        dialog.window!!.setGravity(Gravity.BOTTOM)
//        dialog.window!!.setLayout(
//            WindowManager.LayoutParams.MATCH_PARENT,
//            WindowManager.LayoutParams.WRAP_CONTENT
//        )
//        dialog.setCancelable(true)
//        dialog.show()
//
//        val tvGallery = dialog.findViewById<TextView>(R.id.tvGallery)
//        val tvTakePhoto = dialog.findViewById<TextView>(R.id.tvTakePhoto)
//        val tvViewProfile = dialog.findViewById<TextView>(R.id.tvViewProfilePicture)
//        tvGallery.setOnClickListener(this)
//        tvTakePhoto.setOnClickListener(this)
//        tvViewProfile.setOnClickListener(this)
    }

    fun displayDialog(){
        dialog = Dialog(this.context, R.style.DialogTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_choose_profile_photo)
        dialog.window!!.setGravity(Gravity.BOTTOM)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.setCancelable(true)
        dialog.show()

        val tvGallery = dialog.findViewById<TextView>(R.id.tvGallery)
        val tvTakePhoto = dialog.findViewById<TextView>(R.id.tvTakePhoto)
        val tvViewProfile = dialog.findViewById<TextView>(R.id.tvViewProfilePicture)
        tvGallery.setOnClickListener(this)
        tvTakePhoto.setOnClickListener(this)
        tvViewProfile.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tvGallery -> {
                this.callback.onItemClickListener(GALLERY, v, dialog)
            }
            R.id.tvTakePhoto -> {
                this.callback.onItemClickListener(CAMERA, v, dialog)
            }
            R.id.tvViewProfilePicture -> {
                this.callback.onItemClickListener(VIEW_PROFILE_PICTURE, v, dialog)
            }
            else -> {
                Toast.makeText(context, "Nothing clicked...", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private val GALLERY = 1
        private val CAMERA = 2
        private val VIEW_PROFILE_PICTURE = 3
    }
}
