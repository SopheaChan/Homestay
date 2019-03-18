package com.example.homestay.ui.login

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.example.homestay.R
import com.example.homestay.custom.DialogDisplayLoadingProgress
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*


class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private val loginMvpPresenter: LoginMvpPresenter = LoginPresenter(this@LoginActivity)
    private val dialogLoadingProgress: DialogDisplayLoadingProgress = DialogDisplayLoadingProgress(this@LoginActivity)
    val EMAIL = "email"
    private lateinit var callbackManager: CallbackManager
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

        callbackManager = CallbackManager.Factory.create()
        val currentAccessToken = AccessToken.getCurrentAccessToken()
        val isLogin: Boolean = currentAccessToken != null && !currentAccessToken.isExpired
        Log.e("Is Loggin with fb: ", isLogin.toString())

        loginMvpPresenter.checkUserLoginState(this, dialogLoadingProgress)
//        printKeyHash(this)
    }

    override fun onClick(v: View?) {
        val buttonID = v?.id
        when (buttonID) {
            R.id.btnLogin -> {
                loginMvpPresenter.setButtonLoginListener(etEmail, etPassword, dialogLoadingProgress)
            }
            R.id.btnSignUp -> {
                loginMvpPresenter.setButtonSignUpListener()
            }
            R.id.btnFbLogin -> {
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList(EMAIL))
                LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                    var userName: String ?= ""
                    var gender: String ?= ""
                    var email: String ?= ""
                    override fun onSuccess(result: LoginResult?) {
                        val request: GraphRequest = GraphRequest.newMeRequest(
                            AccessToken.getCurrentAccessToken()
                        ) { `object`, response ->
                            userName = `object`?.getString("name")
                            Toast.makeText(this@LoginActivity, userName, Toast.LENGTH_SHORT).show()
                        }
                        var parameter = Bundle()
                        parameter.putString("fields", userName)
                        request.parameters = parameter
                        request.executeAsync()
                    }

                    override fun onCancel() {
                        Toast.makeText(this@LoginActivity, "Canceled...", Toast.LENGTH_SHORT).show()
                    }

                    override fun onError(error: FacebookException?) {
                        Toast.makeText(this@LoginActivity, "Canceled...", Toast.LENGTH_SHORT).show()
                    }

                })
            }
            else -> return
        }
    }

    fun printKeyHash(context: Activity): String? {
        val packageInfo: PackageInfo
        var key: String? = null
        try {
            //getting application package name, as defined in manifest
            val packageName = context.applicationContext.packageName

            //Retriving package info
            packageInfo = context.packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_SIGNATURES
            )

            Log.e("Package Name=", context.applicationContext.packageName)

            for (signature in packageInfo.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                key = String(Base64.encode(md.digest(), 0))

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key)
            }
        } catch (e1: PackageManager.NameNotFoundException) {
            Log.e("Name not found", e1.toString())
        } catch (e: NoSuchAlgorithmException) {
            Log.e("No such an algorithm", e.toString())
        } catch (e: Exception) {
            Log.e("Exception", e.toString())
        }

        return key
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}
