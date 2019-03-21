package com.example.homestay.ui.hotel_detail

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.example.homestay.R
import com.example.homestay.custom.CustomDialog
import com.example.homestay.custom.DialogDisplayLoadingProgress
import com.example.homestay.custom.DialogMenu
import com.example.homestay.model.FavoriteList
import com.example.homestay.ui.book_hotel.BookHotelActivity
import com.example.homestay.ui.home.HomePresenter
import com.example.homestay.ui.maps.MapsActivity
import com.example.homestay.utils.GetCurrentDateTime
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.model.SharePhoto
import com.facebook.share.model.SharePhotoContent
import com.facebook.share.widget.ShareButton
import com.facebook.share.widget.ShareDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class HotelDetailPresenter(private val activity: Activity) : HotelDetailMvpPresenter {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun addToFavorite(context: Activity, hotelName: String, hotelAddress: String, imageUrl: String) {
        val firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        val userID: String = firebaseUser!!.uid
        val issueDate: String = GetCurrentDateTime.getAddedToFavoriteListOnDate() ?: "N/A"
        val favoriteList = FavoriteList(hotelName, hotelAddress, issueDate, imageUrl)
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("favorite")
            .child(userID)
            .child(issueDate)
        databaseReference.setValue(favoriteList)
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                Toast.makeText(context.baseContext, "Success...", Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun setActionToItemClick(
        v: View?,
        itemButtonViewHotelLocation: Int,
        itemButtonViewHotelDescription: Int,
        itemButtonBookHotel: Int,
        customDialog: CustomDialog,
        hotelName: String,
        hotelAddress: String
    ) {
        when (v?.id) {
            R.id.btnViewHotelLocation -> {
                activity.startActivity(Intent(activity.baseContext, MapsActivity::class.java))
            }
            R.id.btnViewHotelDescription -> {
//                customDialog.displayDialog(R.layout.dialog_view_hotel_description, R.style.DialogTheme)
                if (checkCameraPermission()){
                    openCamera()
                }
            }
            R.id.btnBookHotel -> {
                val intent = Intent(activity.baseContext, BookHotelActivity::class.java)
                intent.putExtra("hotel_name", hotelName)
                intent.putExtra("hotel_address", hotelAddress)
                activity.startActivity(intent)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun setActionToSelectedItem(
        item: MenuItem?,
        itemShare: Int,
        itemAddToFavorite: Int,
        hotelName: String,
        hotelAddress: String,
        listImage: ArrayList<String>
    ) {
        when (item?.itemId) {
            R.id.share -> {
                selectImageFromGallery()
            }
            R.id.add_to_favorite -> {
                val title = item.title
                if (title == "Mark as favorite") {
                    item.setIcon(R.drawable.ic_favorite_border_light_pink_24dp)
                    item.title = "Added to favorite"
                    addToFavorite(activity, hotelName, hotelAddress, listImage[0])
                } else if (title == "Added to favorite") {
                    item.setIcon(R.drawable.ic_menu_favorite_white)
                    item.title = "Mark as favorite"
                }
            }
        }
    }

    //...........................//
    //Share image from Gallery to Facebook
    //...........................//
    private fun selectImageFromGallery() {
        val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        activity.startActivityForResult(intent, GALLERY)
    }

    override fun getActivityResult(requestCode: Int, resultCode: Int, data: Intent, shareDialog: ShareDialog) {
        when (requestCode) {
            GALLERY -> {
                val selectedImage = data.data
                val projection = arrayOf(MediaStore.MediaColumns.DATA)

                val cursor: Cursor = activity.managedQuery(
                    selectedImage, projection, null, null,
                    null
                )
                val column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
                cursor.moveToFirst()
                val selectedImagePath = cursor.getString(column_index)
                Log.e("selectedImage path:", selectedImagePath)
                var thumbnail: Bitmap
                val options: BitmapFactory.Options = BitmapFactory.Options()
                options.inJustDecodeBounds = true
                BitmapFactory.decodeFile(selectedImagePath, options)
                val REQUIRED_SIZE = 200
                var scale = 1
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                    && options.outHeight / scale / 2 >= REQUIRED_SIZE
                )
                    scale *= 2
                options.inSampleSize = scale
                options.inJustDecodeBounds = false
                thumbnail = BitmapFactory.decodeFile(selectedImagePath, options)
                ShareDialog(thumbnail, shareDialog)
            }
            CAMERA -> {
                getCaptureImageResult(data, shareDialog)
            }
        }
    }
    //...............end.............//


    //..................................//
    //Capture image and share to Facebook//
    //..................................//
    private fun checkCameraPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                activity.baseContext,
                android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(android.Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION
            )
            return false
        }
        return true
    }

    private fun checkStoragePermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(activity.baseContext, android.Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                activity.baseContext,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), REQUEST_GALLERY_PERMISSION
            )
            return false
        }
        return true
    }

    private fun openCamera() {
        val intentOpenCamera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intentOpenCamera.resolveActivity(activity.packageManager) != null) {
            activity.startActivityForResult(intentOpenCamera, CAMERA)
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
    //...............end.............//


    //...................................//
    //......Implement share dialog......//
    //.................................//
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
    //...............end.............//

    override fun getRequestPermissionResult(requestCode: Int) {
        when (requestCode) {
            REQUEST_CAMERA_PERMISSION -> {
                openCamera()
            }
            REQUEST_GALLERY_PERMISSION -> {
                selectImageFromGallery()
            }
        }
    }

    companion object {
        const val CAMERA = 2
        const val GALLERY = 1
        const val REQUEST_CAMERA_PERMISSION = 3
        const val REQUEST_GALLERY_PERMISSION = 4
    }
}