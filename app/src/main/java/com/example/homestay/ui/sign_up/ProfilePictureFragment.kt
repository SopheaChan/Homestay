package com.example.homestay.ui.sign_up

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.AppCompatButton
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.homestay.R
import com.example.homestay.custom.DialogMenu
import com.example.homestay.listener.OnDialogMenuClickListener
import com.example.homestay.model.UserBasicInfo
import com.example.homestay.model.UserContact
import com.example.homestay.utils.MyObject
import com.example.homestay.utils.RequestCode
import kotlinx.android.synthetic.main.layout_sign_up_profile_picture.*
import android.graphics.Bitmap
import android.R.attr.data
import android.R.attr.dial
import android.support.v4.app.NotificationCompat.getExtras
import com.example.homestay.custom.DialogDisplayLoadingProgress
import com.example.homestay.model.User
import java.io.ByteArrayOutputStream


class ProfilePictureFragment : Fragment(), View.OnClickListener {

    private lateinit var signUpMvpPresenter: SignUpMvpPresenter
    private lateinit var btnBack: FloatingActionButton
    private lateinit var userBasicInfo: UserBasicInfo
    private lateinit var userContact: UserContact
    private lateinit var fragment: Fragment
    private lateinit var imageDialog : DialogMenu
    private lateinit var path: String
    private var password: String = " "
    private lateinit var dialogLoadingProgress: DialogDisplayLoadingProgress
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.layout_sign_up_profile_picture, container, false)
        val btnBrowseImage = view.findViewById<AppCompatButton>(R.id.btnBrowse)
        btnBack = view.findViewById(R.id.btnBack2)
        btnBrowseImage.setOnClickListener(this)
        btnBack.setOnClickListener(this)
        fragment = ContactInfoFragment()
        signUpMvpPresenter = SignUpPresenter(view.context)
        dialogLoadingProgress = DialogDisplayLoadingProgress(view.context)
        val bundle: Bundle ?= arguments
        if (bundle != null){
            userBasicInfo = bundle.getSerializable("basicInfo") as UserBasicInfo
            userContact = bundle.getSerializable("contact") as UserContact
            password = bundle.getString("password") as String
            Toast.makeText(context, userContact.email + userBasicInfo.name, Toast.LENGTH_SHORT).show()
        }
        imageDialog = DialogMenu(view.context, dialogClickListener)
        return view
    }

    override fun onClick(v: View?) {
        when (v?.id){
            R.id.btnBrowse -> {
                Log.e("userBasicInfo:", userBasicInfo.name +", "+ userContact.phone+".........................")
                imageDialog.displayDialog()
            }
            R.id.btnBack2 -> {
                signUpMvpPresenter.onButtonBackListener(MyObject.getFragmentManager(), fragment)
            }
        }
    }

    private val dialogClickListener= OnDialogMenuClickListener { result, view, dialog ->
        chooseProfile(result, view, dialog)
    }

    private fun chooseProfile(result: Int, view: View?, dialog: Dialog?) {
        when (result){
            RequestCode.CHOOSE_FROM_GALLERY -> {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, RequestCode.GALLLERY)
                dialog?.dismiss()
            }
            RequestCode.TAKE_PHOTO -> {
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (takePictureIntent.resolveActivity(context!!.packageManager) != null) {
                    startActivityForResult(takePictureIntent, RequestCode.CAMERA)
                    dialog?.dismiss()
                } else{
                    Toast.makeText(context, "Failed to start camera...", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RequestCode.GALLLERY && resultCode == Activity.RESULT_OK){
            val profile: Uri ?= data?.data
            imgProfilePicture.setImageURI(profile)
            val user = User(userBasicInfo, userContact, profile.toString())
            signUpMvpPresenter.saveUserToDatabase(user, password, context, dialogLoadingProgress)
            dialogLoadingProgress.displayLoadingProgressRecursive("Saving user data...")
        } else if (requestCode == RequestCode.CAMERA && resultCode == Activity.RESULT_OK){
            val extras = data?.extras
            val imageBitmap = extras?.get("data") as Bitmap

            val os = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, os)
            path = MediaStore.Images.Media.insertImage(context!!.contentResolver, imageBitmap, "profile_picture", null)
            imgProfilePicture.setImageBitmap(imageBitmap)
            imgProfilePicture.setImageBitmap(imageBitmap)
            val user = User(userBasicInfo, userContact, path)
            signUpMvpPresenter.saveUserToDatabase(user, password, context, dialogLoadingProgress)
            dialogLoadingProgress.displayLoadingProgressRecursive("Saving user data...")
        }
    }
}