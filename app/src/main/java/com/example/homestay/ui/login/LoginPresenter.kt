package com.example.homestay.ui.login

import android.app.Activity
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import com.example.homestay.custom.DialogDisplayLoadingProgress
import com.example.homestay.ui.home.HomeActivity
import com.example.homestay.ui.home.HomePresenter
import com.example.homestay.ui.hotel_detail.HotelDetailPresenter
import com.example.homestay.ui.sign_up.SignUpActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.facebook.share.model.SharePhoto
import com.facebook.share.model.SharePhotoContent
import com.facebook.share.widget.ShareDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.io.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.text.Typography.amp

class LoginPresenter(private val context: Activity) : LoginMvpPresenter {
    lateinit var mFirebaseAuth: FirebaseAuth
//    private val GALLERY = 1

    override fun setButtonLoginListener(
        etEmail: EditText,
        etPassword: EditText,
        dialogDisplayLoadingProgress: DialogDisplayLoadingProgress
    ) {
        val email: String = etEmail.text.toString()
        val password: String = etPassword.text.toString()
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches() || password.isEmpty() || password.length < 6) {
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.error = "invalid email address"
                etEmail.requestFocus()
            }
            if (password.isEmpty() || password.length < 6) {
                etPassword.error = "invalid email address"
            }
            return
        } else {
            dialogDisplayLoadingProgress.displayLoadingProgressRecursive("Signing in...")
            mFirebaseAuth = FirebaseAuth.getInstance()
            mFirebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        context.startActivity(Intent(context, HomeActivity::class.java))
                        context.finish()
                        dialogDisplayLoadingProgress.getDialog().dismiss()
                    } else {
                        dialogDisplayLoadingProgress.getDialog().dismiss()
                        Toast.makeText(context, "Failed signing in...", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    dialogDisplayLoadingProgress.getDialog().dismiss()
                    Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun setButtonSignUpListener() {
        context.startActivity(Intent(context, SignUpActivity::class.java))
//        initShareIntent("", "Image")
//        selectImageFromGallery()
    }

    override fun checkUserLoginState(context: Activity, dialog: DialogDisplayLoadingProgress) {
        mFirebaseAuth = FirebaseAuth.getInstance()
        val mFirebaseUser: FirebaseUser? = mFirebaseAuth.currentUser
        if (mFirebaseUser != null) {
            context.startActivity(Intent(context, HomeActivity::class.java))
            context.finish()
        }
    }

    override fun loginWithFacebook() {
        val EMAIL = "email"
        val loginButton = LoginButton(context.baseContext)
        val callBackManager = CallbackManager.Factory.create()
        loginButton.setReadPermissions(Arrays.asList(EMAIL))
        loginButton.registerCallback(callBackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                Toast.makeText(context.baseContext, result?.accessToken.toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onCancel() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onError(error: FacebookException?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
    }

    /*private fun initShareIntent(type:String, _text:String) {
        val filePath = context.getFileStreamPath("shareimage.jpg") //optional //internal storage
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_TEXT, _text)
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(filePath)) //optional//use this when you want to send an image
        shareIntent.type = "image/jpeg"
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        context.startActivity(Intent.createChooser(shareIntent, "send"))
    }*/



    /*private fun selectImageFromGallery(){
        val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        context.startActivityForResult(intent, GALLERY)
    }

    override fun getActivityResult(requestCode: Int, resultCode: Int, data: Intent, shareDialog: ShareDialog) {
        if (requestCode == GALLERY) {
            val selectedImage = data.data
            val projection = arrayOf(MediaStore.MediaColumns.DATA)

            val cursor: Cursor = context.managedQuery(
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
    }
        private fun ShareDialog(imagePath: Bitmap, shareDialog: ShareDialog){
            val photo = SharePhoto.Builder()
                .setBitmap(imagePath)
                .setCaption("Test...")
                .build()
            val content = SharePhotoContent.Builder()
                .addPhoto(photo)
                .build()
            shareDialog.show(content)
        }*/

}