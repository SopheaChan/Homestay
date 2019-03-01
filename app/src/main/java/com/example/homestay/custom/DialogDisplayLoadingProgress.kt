package com.example.homestay.custom

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.example.homestay.R
import de.hdodenhof.circleimageview.CircleImageView

import java.util.Objects

class DialogDisplayLoadingProgress(private val context: Context) {
    private lateinit var dialog: Dialog
    fun displayLoadingProgressTimeDefined(title: String) {
        dialog = Dialog(context, R.style.DialogTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_loading_progress)
        dialog.window?.setGravity(Gravity.CENTER)
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog!!.show()

        val imgLoading = dialog.findViewById<ImageView>(R.id.imgLoadingProgress)
        val tvTitle = dialog.findViewById<TextView>(R.id.tvLoadingLabel)
        tvTitle.text = title
        val animation = AnimationUtils.loadAnimation(context, R.anim.rotate)
        imgLoading.animation = animation
        animation.duration = 5000
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {

            }

            override fun onAnimationEnd(animation: Animation) {
                getDialog().dismiss()
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })

    }
    fun getDialog(): Dialog{
        return this.dialog
    }

    fun displayLoadingProgressRecursive(title: String){
        dialog = Dialog(context, R.style.DialogTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_loading_progress)
        dialog.window?.setGravity(Gravity.CENTER)
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog!!.show()

        val imgLoading = dialog.findViewById<ImageView>(R.id.imgLoadingProgress)
        val tvTitle = dialog.findViewById<TextView>(R.id.tvLoadingLabel)
        tvTitle.text = title
        setAnimation(imgLoading)
    }

    fun setAnimation(imgLoading: ImageView){
        val animation = AnimationUtils.loadAnimation(context, R.anim.rotate)
        imgLoading.animation = animation
        animation.duration = 3000
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {

            }

            override fun onAnimationEnd(animation: Animation) {
                setAnimation(imgLoading)
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })
    }
}
