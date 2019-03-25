package com.example.homestay.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.WindowManager
import com.example.homestay.R
import com.example.homestay.helper.QRHelper
import com.example.homestay.model.DataForQRTesting
import com.example.homestay.utils.StoreCurrentUserInfo
import com.google.gson.Gson
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import kotlinx.android.synthetic.main.activity_qrcode_generator.*

class QRCodeGenerator : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        setContentView(R.layout.activity_qrcode_generator)

//        val valueA = intent.getStringExtra("valueA")
//        val valueB = intent.getStringExtra("valueB")
//        generateDataToQRCode(valueA, valueB)
        generateDataToQRCode()
    }

    private fun generateDataToQRCode() {
            val serializeString = Gson().toJson(StoreCurrentUserInfo.getUser().userBasicInfo)
//        val deserializeData = Gson().fromJson(serializeString, DataForQRTesting::class.java)
            /*val a = deserializeData.valueA
            val b = serializeString[7]
            Log.e("data:", "............................+$serializeString")
            Log.e("data:", "............................+$a + $b")*/
            val bitMap= QRHelper
                .newInstance(this)
                .setContent(serializeString)
                .setErrorCorrectionLevel(ErrorCorrectionLevel.Q)
                .qrcOde
            qrCodeImageView.setImageBitmap(bitMap)
    }

    /*private fun generateDataToQRCode(a: String, b: String) {
        val data = DataForQRTesting(a, b)
        val serializeString = Gson().toJson(data)
//        val deserializeData = Gson().fromJson(serializeString, DataForQRTesting::class.java)
        *//*val a = deserializeData.valueA
        val b = serializeString[7]
        Log.e("data:", "............................+$serializeString")
        Log.e("data:", "............................+$a + $b")*//*
        val bitMap= QRHelper
            .newInstance(this)
            .setContent(serializeString)
            .setErrorCorrectionLevel(ErrorCorrectionLevel.Q)
            .qrcOde
        qrCodeImageView.setImageBitmap(bitMap)
    }*/
}
