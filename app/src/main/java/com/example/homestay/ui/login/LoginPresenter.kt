package com.example.homestay.ui.login

import android.content.Context
import android.content.Intent
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import com.example.homestay.custom.DialogDisplayLoadingProgress
import com.example.homestay.ui.home.HomeActivity
import com.example.homestay.ui.sign_up.SignUpActivity

class LoginPresenter(private val context: Context) : LoginMvpPresenter {

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
            dialogDisplayLoadingProgress.displayLoadingProgressTimeDefined("Signing in...")
            context.startActivity(Intent(context, HomeActivity::class.java))
            dialogDisplayLoadingProgress.getDialog().dismiss()
        }
    }

    override fun setButtonSignUpListener() {
        context.startActivity(Intent(context, SignUpActivity::class.java))
    }
}