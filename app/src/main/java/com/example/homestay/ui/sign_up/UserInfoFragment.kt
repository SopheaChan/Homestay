package com.example.homestay.ui.sign_up

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.AppCompatButton
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.homestay.R
import com.example.homestay.model.UserBasicInfo
import com.example.homestay.utils.MyObject
import kotlinx.android.synthetic.main.layout_sign_up_user_info.*

class UserInfoFragment : Fragment(), View.OnClickListener {

    private lateinit var btnNext: AppCompatButton

    private lateinit var signUpMvpPresenter: SignUpMvpPresenter
    private lateinit var userBasicInfo: UserBasicInfo

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.layout_sign_up_user_info, container, false)
        btnNext = view.findViewById(R.id.btnNext)
        btnNext.setOnClickListener(this)
        signUpMvpPresenter = SignUpPresenter(view.context)
        return view
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnNext -> {
                val fragment = ContactInfoFragment()
                val uName: String = etName.text.toString().trim()
                val uSex: String = etSex.text.toString().trim()
                val uAge: String = etAge.text.toString().trim()
                val uAddress: String = etCountry.text.toString().trim() + ", " + etProvince.text.toString().trim() + ", " +
                        etDistrict.text.toString().trim() + ", " + etVillage.text.toString().trim()

                if (uName.isEmpty() || uSex.isEmpty() || uAge.isEmpty() || uAddress.isEmpty() ||
                    (!uAddress.contains(","))) {
                    if (etName.text.isEmpty()) etName.error = "required"
                    if (etSex.text.isEmpty()) etSex.error = "required"
                    if (etAge.text.isEmpty()) etAge.error = "required"
                    if (etCountry.text.isEmpty()) etCountry.error = "required"
                    if (etProvince.text.isEmpty()) etProvince.error = "required"
                    if (etDistrict.text.isEmpty()) etDistrict.error = "required"
                    if (etVillage.text.isEmpty()) etVillage.error = "required"
                } else {
                    val userBasicInfo = UserBasicInfo(uName, uSex, uAge, uAddress)
                    val bundle= Bundle()
                    bundle.putSerializable("basicInfo", userBasicInfo)
                    signUpMvpPresenter.onButtonNextListener(MyObject.getFragmentManager(), fragment, bundle)
                }
            }
            else -> return
        }
    }

    fun setTextToEditText(text: String): Editable{
        return Editable.Factory.getInstance().newEditable(text)
    }
}