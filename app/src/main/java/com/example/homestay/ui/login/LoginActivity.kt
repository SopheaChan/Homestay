package com.example.homestay.ui.login

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.media.session.MediaSession
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.example.homestay.R
import com.example.homestay.custom.DialogDisplayLoadingProgress
import com.example.homestay.ui.home.HomeActivity
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.facebook.share.widget.ShareDialog
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.*
import com.twitter.sdk.android.core.*
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*


class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private val loginMvpPresenter: LoginMvpPresenter = LoginPresenter(this@LoginActivity)
    private val dialogLoadingProgress: DialogDisplayLoadingProgress = DialogDisplayLoadingProgress(this@LoginActivity)
    val EMAIL = "email"
    val PUBLIC_PROFILE = "public_profile"
    val PUBLIC_ACTION = "public_action"
    lateinit var mAuth: FirebaseAuth
    private lateinit var callbackManager: CallbackManager
    private lateinit var shareDialog: ShareDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Twitter.initialize(this)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        setContentView(R.layout.activity_login)
        btnLogin.setOnClickListener(this)
        btnSignUp.setOnClickListener(this)
        btnFbLogin.setOnClickListener(this)
        btnTwitterLogin.setOnClickListener(this)

        callbackManager = CallbackManager.Factory.create()
//        shareDialog = ShareDialog(this)
        val currentAccessToken = AccessToken.getCurrentAccessToken()
        val isLogin: Boolean = currentAccessToken != null && !currentAccessToken.isExpired
        Log.e("Is Loggin with fb: ", isLogin.toString())

        loginMvpPresenter.checkUserLoginState(this, dialogLoadingProgress)
        accessTokenTracker()
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
                dialogLoadingProgress.displayLoadingProgressRecursive("Logging in...")
                btnFbLogin.setReadPermissions(Arrays.asList(EMAIL,PUBLIC_PROFILE))
                btnFbLogin.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                    var userName: String ?= ""
                    var gender: String ?= ""
                    var email: String = ""
                    override fun onSuccess(result: LoginResult?) {
                        val token = AccessToken.getCurrentAccessToken()
                        val request: GraphRequest = GraphRequest.newMeRequest(
                            AccessToken.getCurrentAccessToken()
                        ) { `object`, response ->
                            userName = `object`?.getString("name")
//                            email = `object`?.get("email") as String
                            handleFacebookAccessToken(result?.accessToken!!)
                           Log.e("Token: ", userName + email )
                        }
                        var parameter = Bundle()
                        parameter.putString("fields", "name,email")
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
            R.id.btnTwitterLogin -> {
                btnTwitterLogin.callback = object : Callback<TwitterSession>(){
                    override fun success(result: Result<TwitterSession>?) {
                        Log.e("Twitter Login state: ${result?.data}", "")
                        handleTwitterSession(result!!.data)
                    }

                    override fun failure(exception: TwitterException?) {
                        Toast.makeText(this@LoginActivity, "Failed...", Toast.LENGTH_SHORT).show()
                    }

                }
            }
            else -> return
        }
    }

    private fun handleTwitterSession(session: TwitterSession) {
        Log.d("Twitter session", "handleTwitterSession:$session")

        val credential = TwitterAuthProvider.getCredential(
            session.authToken.token,
            session.authToken.secret)

        /*auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }

                // ...
            }*/
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential: AuthCredential = FacebookAuthProvider.getCredential(token.token)
        val auth = FirebaseAuth.getInstance()
        var userID: String
        var firebaseUser: FirebaseUser?
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    /*startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                    Log.e("Sign in result: ", "signInWithCredential:success")*/
                    dialogLoadingProgress.getDialog().dismiss()
                    firebaseUser = auth.currentUser
                    userID = firebaseUser!!.uid
                    Log.e("Facebook auth state: ", "success $userID")

                } else {
                    // If sign in fails, display a message to the user.
                    Log.e("Sign in result: ", "signInWithCredential:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun accessTokenTracker() {
        object : AccessTokenTracker(){
            override fun onCurrentAccessTokenChanged(oldAccessToken: AccessToken?, currentAccessToken: AccessToken?) {
                if (currentAccessToken == null){
                    dialogLoadingProgress.getDialog().dismiss()
                    Log.e("Log out successfully...","")
                }
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        btnTwitterLogin.onActivityResult(requestCode, resultCode, data)
//        loginMvpPresenter.getActivityResult(requestCode, resultCode, data!!, shareDialog)
        super.onActivityResult(requestCode, resultCode, data)
    }
}
