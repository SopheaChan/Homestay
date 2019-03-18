package com.example.homestay.ui.login

import android.app.Activity
import android.widget.EditText
import com.example.homestay.custom.DialogDisplayLoadingProgress

interface LoginMvpPresenter {
    fun setButtonLoginListener(
        etEmail: EditText,
        etPassword: EditText,
        dialogDisplayLoadingProgress: DialogDisplayLoadingProgress
    )
    fun setButtonSignUpListener()
    fun checkUserLoginState(context: Activity, dialog: DialogDisplayLoadingProgress)
    fun loginWithFacebook()
}