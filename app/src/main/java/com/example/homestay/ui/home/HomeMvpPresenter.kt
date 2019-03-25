package com.example.homestay.ui.home

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.icu.util.MeasureUnit
import android.net.Uri
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.AppCompatButton
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.budiyev.android.codescanner.CodeScannerView
import com.example.homestay.adapter.HomeAdapter
import com.example.homestay.custom.CustomDialog
import com.example.homestay.custom.DialogDisplayLoadingProgress
import com.example.homestay.custom.DialogMenu
import com.example.homestay.model.FavoriteList
import com.example.homestay.model.HotelData
import de.hdodenhof.circleimageview.CircleImageView

interface HomeMvpPresenter {
    fun setDataToList(hotelList: MutableList<HotelData>, adapter: HomeAdapter)

    fun onRecyclerImagesClicked(hotelData: HotelData, imageView: ImageView)
    fun onLoadUser(tvUserName: TextView, tvEmail: TextView, imgProfile: CircleImageView)
    fun onSignOut(loadingProgress: DialogDisplayLoadingProgress)
    //    fun onUploadPhoto(newImage: Uri, dialogLoadingProgress: DialogDisplayLoadingProgress)
//    fun onViewProfilePicture(imgImageView: ImageView, btnDone: AppCompatButton, dialog: CustomDialog)

    fun onViewFavoriteList(layoutID: Int, layoutStyle: Int, customDialog: CustomDialog)
    fun onLoadBookingInfo()
    fun onLoadFavoriteList()
    fun onOpenGallery()
    //    fun onOpenCamera()
    fun onListFilter(inputText: String, listHotel: MutableList<HotelData>, homeAdapter: HomeAdapter)

    fun onPermissionResult(requestCode: Int)
    fun getActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        imgUserProfile: CircleImageView,
        dialogMenu: DialogMenu,
        dialogDisplayLoadingProgress: DialogDisplayLoadingProgress
    )

    fun onDialogMenuCallback(result: Int, dialog: Dialog, dialogLayout: Int, dialogStyle: Int, animation: Int)
    fun setActionToOptionsItem(
        menuItem: MenuItem,
        itemKhmerLanguage: Int,
        itemEnglishLanguage: Int,
        itemSearch: Int,
        itemNearbyHotel: Int,
        tvTitle: TextView,
        etSearch: EditText,
        miSearch: MenuItem
    )

    fun setActionToNavigationItem(
        item: MenuItem,
        itemSignOut: Int,
        itemAboutUs: Int,
        itemFavorite: Int,
        itemProfile: Int,
        itemBooking: Int,
        itemQrScanner: Int,
        itemQRGenerator: Int,
        drawer_layout: DrawerLayout,
        dialogDisplayLoadingProgress: DialogDisplayLoadingProgress,
        customDialog: CustomDialog)
}