package com.example.homestay.ui.login

import android.app.Activity
import android.content.Intent
import android.widget.EditText
import com.example.homestay.custom.DialogDisplayLoadingProgress
import com.facebook.share.widget.ShareDialog

interface LoginMvpPresenter {
    fun setButtonLoginListener(
        etEmail: EditText,
        etPassword: EditText,
        dialogDisplayLoadingProgress: DialogDisplayLoadingProgress
    )
    fun setButtonSignUpListener()
    fun checkUserLoginState(context: Activity, dialog: DialogDisplayLoadingProgress)
    fun loginWithFacebook()
//    fun getActivityResult(requestCode: Int, resultCode: Int, data: Intent, shareDialog: ShareDialog)
}