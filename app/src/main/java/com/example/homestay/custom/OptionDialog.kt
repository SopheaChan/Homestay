package com.example.homestay.custom

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import com.example.homestay.R

class OptionDialog(private val context: Context) {
    private lateinit var dialog: Dialog

    fun displayDialog(){
        dialog = Dialog(context, R.style.DialogTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window.setContentView(R.layout.custom_menu_layout)
        dialog.window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.window.setGravity(Gravity.NO_GRAVITY)
        dialog.show()

    }
}