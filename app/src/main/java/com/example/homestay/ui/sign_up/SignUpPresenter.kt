package com.example.homestay.ui.sign_up

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.util.Log
import android.widget.Toast
import com.example.homestay.R
import com.example.homestay.custom.DialogDisplayLoadingProgress
import com.example.homestay.model.User
import com.example.homestay.ui.home.HomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask

class SignUpPresenter(private val context: Context) : SignUpMvpPresenter {
    private val firebaseAuth: FirebaseAuth
    private val storageReference: StorageReference
    private val databaseReference: DatabaseReference
    private val firebaseUser: FirebaseUser?
    override fun onButtonBackListener(fragmentManager: FragmentManager, fragment: Fragment) {
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.layout_fragment_frame, fragment)
        fragmentTransaction.commitAllowingStateLoss()
    }

    init {
        this.firebaseAuth = FirebaseAuth.getInstance()
        this.storageReference = FirebaseStorage.getInstance().getReference("profile")
        this.databaseReference = FirebaseDatabase.getInstance().reference
        this.firebaseUser = firebaseAuth.currentUser
    }

    override fun onButtonNextListener(fragmentManager: FragmentManager, fragment: Fragment, bundle: Bundle) {
        fragment.arguments = bundle
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.layout_fragment_frame, fragment)
        fragmentTransaction.commitAllowingStateLoss()
    }

    override fun saveUserToDatabase(
        users: User,
        password: String,
        context: Context?,
        dialogLoading: DialogDisplayLoadingProgress
    ) {
        users.userContact?.email?.let {
            firebaseAuth.createUserWithEmailAndPassword(it, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val uProfile: Uri = Uri.parse(users.uProfile)
                        val userID: String = firebaseUser?.uid ?: "$it"
    //                    saveUserProfile(users.uProfile, users, dialogLoading)
    //                    Log.e("User:", users.uProfile + " "+ users.userContact.email)
                        val storageTask = storageReference.child(userID).putFile(uProfile)
                        storageTask.addOnCompleteListener { it1 ->
                            if (it1.isSuccessful) {
                                FirebaseStorage.getInstance().getReference("profile").child(userID)
                                    .downloadUrl
                                    .addOnCompleteListener { imageUri ->
                                        if (imageUri.isSuccessful) {
                                            val imageUrl: Uri? = imageUri.result
                                            databaseReference.child("profile").child(userID)
                                            databaseReference.push()
                                            users.uProfile = imageUrl.toString()
                                            FirebaseDatabase.getInstance().getReference("profile")
                                                .child(userID)
                                                .setValue(users)
                                                .addOnCompleteListener {
                                                    if (it.isSuccessful) {
                                                        context!!.startActivity(Intent(context, HomeActivity::class.java))
                                                        dialogLoading.getDialog().dismiss()
                                                    } else dialogLoading.getDialog().dismiss()
                                                }
                                                .addOnFailureListener {
                                                    dialogLoading.getDialog().dismiss()
                                                }
                                        }
                                    }
                            } else dialogLoading.getDialog().dismiss()
                        }
                            .addOnFailureListener {
                                dialogLoading.getDialog().dismiss()
                            }
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
                    dialogLoading.getDialog().dismiss()
                }
        }
    }
}