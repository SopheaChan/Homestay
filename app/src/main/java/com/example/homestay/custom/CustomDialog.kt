package com.example.homestay.custom

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.SearchView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import com.example.homestay.R
import com.example.homestay.ui.home.HomeMvpView

class CustomDialog constructor(private val context: Context){
    private lateinit var dialog: Dialog

    fun displayDialog(layoutID: Int, dialogStyle: Int){

        dialog = Dialog(context, dialogStyle)
        dialog.window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(layoutID)
        dialog.window?.setGravity(Gravity.CENTER)
        dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT)
        dialog.show()

    }

    fun getDialog(): Dialog{
        return this.dialog
    }
}