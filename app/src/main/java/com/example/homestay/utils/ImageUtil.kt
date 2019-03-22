package com.example.homestay.utils

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import com.example.homestay.ui.hotel_detail.HotelDetailPresenter
import com.facebook.share.model.SharePhoto
import com.facebook.share.model.SharePhotoContent
import com.facebook.share.widget.ShareDialog
import java.io.*
import java.util.jar.Manifest

object ImageUtil {
    const val CAMERA = 1
    const val GALLERY = 2
    const val REQUEST_CAMERA_PERMISSION = 3
    const val REQUEST_GALLERY_PERMISSION = 4

    fun checkCameraPermission(context: Activity) {
        if (ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context,
                arrayOf(android.Manifest.permission.CAMERA),
                REQUEST_CAMERA_PERMISSION
            )
        }
    }

    private fun openCamera(context: Activity) {
        val intentOpenCamera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intentOpenCamera.resolveActivity(context.packageManager) != null) {
            context.startActivityForResult(intentOpenCamera, HotelDetailPresenter.CAMERA)
        }
    }

    private fun getCaptureImageResult(data: Intent, shareDialog: ShareDialog) {
        val thumbnail = data.extras.get("data") as Bitmap
        val bytes = ByteArrayOutputStream()
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val destination =
            File(Environment.getExternalStorageDirectory(), System.currentTimeMillis().toString() + ".jpg")
        var fo: FileOutputStream
        try {
            destination.createNewFile()
            fo = FileOutputStream(destination)
            fo.write(bytes.toByteArray())
            fo.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        ShareDialog(thumbnail, shareDialog)
    }

    private fun ShareDialog(imagePath: Bitmap, shareDialog: ShareDialog) {
        val photo = SharePhoto.Builder()
            .setBitmap(imagePath)
            .setCaption("Test...")
            .build()
        val content = SharePhotoContent.Builder()
            .addPhoto(photo)
            .build()
        shareDialog.show(content)
    }
    fun getRequestPermissionResult(requestCode: Int) {
        when (requestCode) {
            HotelDetailPresenter.REQUEST_CAMERA_PERMISSION -> {
//                openCamera()
            }
            HotelDetailPresenter.REQUEST_GALLERY_PERMISSION -> {
//                selectImageFromGallery()
            }
        }
    }

    private fun selectImageFromGallery(context: Activity) {
        val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        context.startActivityForResult(intent, HotelDetailPresenter.GALLERY)
    }
}