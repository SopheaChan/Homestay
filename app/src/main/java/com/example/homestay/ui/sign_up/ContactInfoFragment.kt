package com.example.homestay.ui.sign_up

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.AppCompatButton
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.homestay.R
import com.example.homestay.utils.MyObject
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.layout_sign_up_user_contact_info.*

class ContactInfoFragment : Fragment(), View.OnClickListener {

    lateinit var btnNext : AppCompatButton
    lateinit var btnBack : AppCompatButton
    private val signUpMvpPresenter : SignUpMvpPresenter = SignUpPresenter()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View = inflater.inflate(R.layout.layout_sign_up_user_contact_info, container, false)
        btnNext = view.findViewById(R.id.btnNext1)
        btnBack = view.findViewById(R.id.btnBack1)
        btnNext.setOnClickListener(this)
        btnBack.setOnClickListener(this)
        return view
    }

    override fun onClick(v: View?) {
        when (v?.id){
            R.id.btnBack1 -> {
                val fragment = UserInfoFragment()
                signUpMvpPresenter.onButtonBackListener(MyObject.getFragmentManager(), fragment)
            }
            R.id.btnNext1 -> {
                val fragment = ProfilePictureFragment()
                if (etTel.text.isEmpty() || etContactEmail.text.isEmpty() ||
                    (!Patterns.EMAIL_ADDRESS.matcher(etContactEmail.text.toString()).matches())){
                    if (etTel.text.isEmpty()) etTel.error = "phone number is invalid"
                    if (etContactEmail.text.isEmpty() || (!Patterns.EMAIL_ADDRESS.matcher(etContactEmail.text.toString()).matches()))
                        etContactEmail.error = "invalid email address"
                } else {
                    signUpMvpPresenter.onButtonNextListener(MyObject.getFragmentManager(), fragment)
                }
            }
        }
    }
}