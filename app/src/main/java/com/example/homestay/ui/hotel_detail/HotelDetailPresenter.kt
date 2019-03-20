package com.example.homestay.ui.hotel_detail

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
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
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

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
                customDialog.displayDialog(R.layout.dialog_view_hotel_description, R.style.DialogTheme)
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
                /*val photo = SharePhoto.Builder()
                    .setBitmap(getBitmap(listImage[0]))
                    .build()
                val content = SharePhotoContent.Builder()
                    .addPhoto(photo)
                    .build()
                val shareButton = ShareButton(activity.baseContext)
                shareButton.shareContent = content*/
                if (checkCameraPermission()){
                    onOpenCamera()
                }
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

    /*private fun getBitmap(src: String): Bitmap?{
        try {
            val url = URL(src)
            val connection: HttpURLConnection  = url.openConnection() as HttpURLConnection
            connection.setDoInput(true)
            connection.connect()
            val input: InputStream = connection.getInputStream()
            val myBitmap = BitmapFactory.decodeStream(input)
            return myBitmap
        }catch (e: IOException){
            Log.e("", e.message)
            return null
        }
    }*/

    private fun checkCameraPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                activity.baseContext,
                android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(android.Manifest.permission.CAMERA),
                REQUEST_CAMERA_PERMISSION
            )
            return false
        }

        return true
    }

     override fun getActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA -> {
                    val extras = data!!.extras
                    val imageBitmap = extras!!.get("data") as Bitmap

                    val os = ByteArrayOutputStream()
                    imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, os)
                    val path =
                        MediaStore.Images.Media.insertImage(
                            activity.contentResolver,
                            imageBitmap,
                            "profile_picture",
                            null
                        )
                    sharePhotoToFacebook(path)
                    Toast.makeText(activity.baseContext, path, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun onOpenCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(activity.packageManager) != null) {
            activity.startActivityForResult(takePictureIntent, CAMERA)
        }
    }

    private fun sharePhotoToFacebook(path: String){
        val shareDialog = ShareDialog(activity)
        if (ShareDialog.canShow(ShareLinkContent::class.java)){
            val linkContent = ShareLinkContent.Builder()
                .setContentTitle("Homestay")
                .setImageUrl(Uri.parse(path))
                .setContentDescription("Hotel Image")
                .build()
            shareDialog.show(linkContent)
        }
    }

    companion object {
        const val REQUEST_CAMERA_PERMISSION = 1
        const val CAMERA = 2
    }
}