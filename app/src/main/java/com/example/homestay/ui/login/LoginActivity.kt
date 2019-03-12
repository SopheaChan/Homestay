package com.example.homestay.ui.login

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import com.example.homestay.R
import com.example.homestay.custom.DialogDisplayLoadingProgress
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private val loginMvpPresenter: LoginMvpPresenter = LoginPresenter(this@LoginActivity)
    private val dialogLoadingProgress: DialogDisplayLoadingProgress = DialogDisplayLoadingProgress(this@LoginActivity)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        setContentView(R.layout.activity_login)
        btnLogin.setOnClickListener(this)
        btnSignUp.setOnClickListener(this)
        btnFbLogin.setOnClickListener(this)

        loginMvpPresenter.checkUserLoginState(this, dialogLoadingProgress)
    }

    override fun onClick(v: View?) {
        val buttonID = v?.id
        when (buttonID) {
            R.id.btnLogin -> {
                loginMvpPresenter.setButtonLoginListener(etEmail, etPassword, dialogLoadingProgress)
            }
            R.id.btnSignUp -> {
                loginMvpPresenter.setButtonSignUpListener() }
            else -> return
        }
    }
}
