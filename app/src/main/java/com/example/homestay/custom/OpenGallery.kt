package com.example.homestay.custom

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore

object OpenGallery {
    private val REQUEST_GALLERY_ACCESS = 4
    fun openGallery(activity: Activity) {
        val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        activity.startActivityForResult(intent, REQUEST_GALLERY_ACCESS)
    }
}