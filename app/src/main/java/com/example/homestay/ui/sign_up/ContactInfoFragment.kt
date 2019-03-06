package com.example.homestay.ui.sign_up

import android.os.Bundle
import android.os.TokenWatcher
import android.support.v4.app.Fragment
import android.support.v7.widget.AppCompatButton
import android.text.Layout
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.homestay.R
import com.example.homestay.model.UserBasicInfo
import com.example.homestay.model.UserContact
import com.example.homestay.utils.MyObject
import kotlinx.android.synthetic.main.layout_sign_up_user_contact_info.*
import kotlinx.android.synthetic.main.layout_sign_up_user_contact_info.view.*
import kotlin.math.ceil

class ContactInfoFragment : Fragment(), View.OnClickListener {

    lateinit var btnNext : AppCompatButton
    lateinit var btnBack : AppCompatButton
    private lateinit var signUpMvpPresenter : SignUpMvpPresenter
    private lateinit var userBasicInfo: UserBasicInfo
    private lateinit var userContact: UserContact
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View = inflater.inflate(R.layout.layout_sign_up_user_contact_info, container, false)
        btnNext = view.findViewById(R.id.btnNext1)
        btnBack = view.findViewById(R.id.btnBack1)
        btnNext.setOnClickListener(this)
        btnBack.setOnClickListener(this)
        signUpMvpPresenter = SignUpPresenter(view.context)
        val bundle: Bundle ?= arguments
        if (bundle != null){
            userBasicInfo = bundle.getSerializable("basicInfo") as UserBasicInfo
        }
        return view
    }

    override fun onClick(v: View?) {
        when (v?.id){
            R.id.btnBack1 -> {
                val fragment: Fragment = UserInfoFragment()
                signUpMvpPresenter.onButtonBackListener(MyObject.getFragmentManager(), fragment)
            }
            R.id.btnNext1 -> {
                val fragment = ProfilePictureFragment()
                val tel: String = etTel.text.toString().trim()
                val email: String = etContactEmail.text.toString().trim()
                val password: String = etPassword.text.toString().trim()
                val conPassword: String = etConfirmPassword.text.toString().trim()

                val otherContact: String = etOtherContact.text.toString().trim()
                if (tel.isEmpty() || email.isEmpty() ||
                    (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                    || password.isEmpty() || conPassword.isEmpty()){
                    if (etTel.text.isEmpty()) etTel.error = "phone number is invalid"
                    if (etContactEmail.text.isEmpty() || (!Patterns.EMAIL_ADDRESS.matcher(etContactEmail.text.toString()).matches()))
                        etContactEmail.error = "invalid email address"

                } else {
                    userContact = UserContact(tel, email, otherContact)
                    val bundle = Bundle()
                    bundle.putSerializable("basicInfo", userBasicInfo)
                    bundle.putSerializable("contact", userContact)
                    bundle.putString("password", etPassword.text.toString())
                    signUpMvpPresenter.onButtonNextListener(MyObject.getFragmentManager(), fragment, bundle)
                }
            }
        }
    }
}