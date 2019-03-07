package com.example.homestay.ui.home

import android.app.Activity
import android.app.Dialog
import android.net.Uri
import android.support.v7.widget.AppCompatButton
import android.widget.ImageView
import android.widget.TextView
import com.example.homestay.adapter.HomeAdapter
import com.example.homestay.custom.CustomDialog
import com.example.homestay.custom.DialogDisplayLoadingProgress
import com.example.homestay.model.FavoriteList
import com.example.homestay.model.HotelData
import de.hdodenhof.circleimageview.CircleImageView

interface HomeMvpPresenter {
    fun setDataToList(hotelList: MutableList<HotelData>, adapter: HomeAdapter)

    fun onRecyclerImagesClicked(context: Activity, hotelData: HotelData, imageView: ImageView)
    fun onLoadUser(tvUserName: TextView, tvEmail: TextView, imgProfile: CircleImageView, context: Activity)
    fun onSignOut(context: Activity, loadingProgress: DialogDisplayLoadingProgress)
    fun onUploadPhoto(context: Activity, newImage: Uri, dialogLoadingProgress: DialogDisplayLoadingProgress)
    fun onViewProfilePicture(imgImageView: ImageView, btnDone: AppCompatButton, context: Activity, dialog: CustomDialog)
    fun onViewFavoriteList(context: Activity, layoutID: Int, layoutStyle: Int, customDialog: CustomDialog)
}