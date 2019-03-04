package com.example.homestay.ui.sign_up

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.view.View
import android.widget.Toast
import com.example.homestay.R
import com.example.homestay.model.User
import com.example.homestay.utils.RequestCode
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class SignUpPresenter : SignUpMvpPresenter {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firebaseUser: FirebaseUser ?= firebaseAuth.currentUser
    private val firebaseStorage: FirebaseStorage = FirebaseStorage.getInstance()
    private val storageReference: StorageReference = firebaseStorage.reference
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference = firebaseDatabase.reference
    override fun onButtonBackListener(fragmentManager: FragmentManager, fragment: Fragment) {
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.layout_fragment_frame, fragment)
        fragmentTransaction.commitAllowingStateLoss()
    }


    override fun onButtonNextListener(fragmentManager: FragmentManager, fragment: Fragment, bundle: Bundle) {
        fragment.arguments = bundle
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.layout_fragment_frame, fragment)
        fragmentTransaction.commitAllowingStateLoss()
    }

    override fun saveUserToDatabase(users: User, password: String, context: Context?) {
        firebaseAuth.createUserWithEmailAndPassword(users.userContact.email,password)
            .addOnCompleteListener{
                if (it.isSuccessful){
                    saveUserProfile(users.uProfile)
                }
            }
            .addOnFailureListener{
                Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveUserProfile(uProfile: String) {
        val userID = firebaseUser!!.uid
        databaseReference.child("profile").child(userID).setValue(uProfile)
            .addOnCompleteListener{
                
            }
    }
}