package com.example.homestay.ui.sign_up

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.AppCompatButton
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.homestay.R
import com.example.homestay.ui.login.LoginActivity
import com.example.homestay.utils.MyObject
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.layout_sign_up_user_info.*

class UserInfoFragment : Fragment(), View.OnClickListener {

    private lateinit var btnNext: AppCompatButton

    private val signUpMvpPresenter: SignUpMvpPresenter = SignUpPresenter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.layout_sign_up_user_info, container, false)
        btnNext = view.findViewById(R.id.btnNext)
        btnNext.setOnClickListener(this)
        return view
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnNext -> {
                val fragment = ContactInfoFragment()

                if (etName.text.isEmpty() || etSex.text.isEmpty() || etAge.text.isEmpty() || etAddress.text.isEmpty()) {
                    if (etName.text.isEmpty()) etName.error = "required"
                    if (etSex.text.isEmpty()) etSex.error = "required"
                    if (etAge.text.isEmpty()) etAge.error = "required"
                    if (etAddress.text.isEmpty()) etAddress.error = "required"
                } else {
                    signUpMvpPresenter.onButtonNextListener(MyObject.getFragmentManager(), fragment)
                }
            }
            else -> return
        }
    }
}