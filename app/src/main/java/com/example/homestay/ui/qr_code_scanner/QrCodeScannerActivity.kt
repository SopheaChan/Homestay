package com.example.homestay.ui.qr_code_scanner

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.example.homestay.R
import com.example.homestay.custom.CustomDialog
import com.example.homestay.model.UserBasicInfo
import com.example.homestay.utils.StoreCurrentUserInfo
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_qr_code_scanner.*

class QrCodeScannerActivity : AppCompatActivity() {

    private lateinit var scanningResult: CustomDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        setContentView(R.layout.activity_qr_code_scanner)

        scanningResult = CustomDialog(this)
        initScanner()
    }

    @SuppressLint("SetTextI18n")
    private fun initScanner(){
        val codeScanner = CodeScanner(this, scanner_view)
        codeScanner.camera = CodeScanner.CAMERA_BACK
        codeScanner.formats = CodeScanner.ALL_FORMATS
        codeScanner.autoFocusMode = AutoFocusMode.SAFE
        codeScanner.isFlashEnabled = false
        codeScanner.isAutoFocusEnabled = true
        codeScanner.scanMode = ScanMode.SINGLE

        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                scanningResult.displayDialog(R.layout.dialog_layout_display_scanner_result, R.style.DialogBookHotelTheme)
                val textDisplayScanningResult = scanningResult.getDialog().findViewById<TextView>(R.id.text_view_user_name)
                val result = Gson().fromJson(it.text, UserBasicInfo::class.java)
                textDisplayScanningResult.text = result.name + result.address + result.age + result.sex
            }
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
                Toast.makeText(this, "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG).show()
            }
        }

        scanner_view.setOnClickListener {
            codeScanner.startPreview()
        }
    }
}
