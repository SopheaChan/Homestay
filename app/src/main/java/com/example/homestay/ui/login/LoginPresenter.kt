package com.example.homestay.ui.login

import android.app.Activity
import android.content.Intent
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import com.example.homestay.custom.DialogDisplayLoadingProgress
import com.example.homestay.ui.home.HomeActivity
import com.example.homestay.ui.sign_up.SignUpActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginPresenter(private val context: Activity) : LoginMvpPresenter {
    lateinit var mFirebaseAuth: FirebaseAuth

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
                .addOnCompleteListener{
                    if (it.isSuccessful){
                        context.startActivity(Intent(context, HomeActivity::class.java))
                        context.finish()
                        dialogDisplayLoadingProgress.getDialog().dismiss()
                    } else{
                        dialogDisplayLoadingProgress.getDialog().dismiss()
                        Toast.makeText(context, "Failed signing in...", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener{
                    dialogDisplayLoadingProgress.getDialog().dismiss()
                    Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun setButtonSignUpListener() {
        context.startActivity(Intent(context, SignUpActivity::class.java))
    }

    override fun checkUserLoginState(context: Activity, dialog: DialogDisplayLoadingProgress) {
        mFirebaseAuth = FirebaseAuth.getInstance()
        val mFirebaseUser: FirebaseUser ?= mFirebaseAuth.currentUser
        if (mFirebaseUser != null){
            context.startActivity(Intent(context, HomeActivity::class.java))
            context.finish()
        }
    }
}