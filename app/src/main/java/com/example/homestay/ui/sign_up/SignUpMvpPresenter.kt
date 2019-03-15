package com.example.homestay.ui.sign_up

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.View
import com.example.homestay.custom.DialogDisplayLoadingProgress
import com.example.homestay.model.User

interface SignUpMvpPresenter {
    fun onButtonBackListener(fragmentManager: FragmentManager, fragment: Fragment)
    fun onButtonNextListener(fragmentManager: FragmentManager, fragment: Fragment, bundle: Bundle)
    fun saveUserToDatabase(users: User, password: String, dialogLoading: DialogDisplayLoadingProgress)
}