package com.example.homestay.custom

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.example.homestay.R
import kotlinx.android.synthetic.main.view_iorder_dialog.view.*

class IOrderDialog(context: Context, message: String) : Dialog(context) {

    private val mView = LayoutInflater.from(context).inflate(R.layout.view_iorder_dialog, null)
    private var positiveListener: IOrderDialogListener? = null
    private var negativeListener: IOrderDialogListener? = null
    private var isCancel = true
    private var isHide = false

    init {
        setContentView(mView)
        setCancelable(true) // default Cancelable

        window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.setWindowAnimations(0)
        window!!.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        mView.background_dialog.setOnClickListener {
            if (isCancel) {
                mView.background_dialog.isEnabled = false
                hideDialog()
            }
        }

        mView.button_positive.setOnClickListener {
            if (positiveListener != null) {
                positiveListener?.onDialogClick(mView.button_positive.text.toString(), this)
            }
            mView.button_positive.isEnabled = false
            hideDialog()
        }

        mView.button_negative.setOnClickListener {
            if (negativeListener != null) {
                negativeListener?.onDialogClick(mView.button_negative.text.toString(), this)
            }
            mView.button_negative.isEnabled = false
            hideDialog()
        }
    }

    fun showOk() {
        setPositiveButton(context.getString(R.string.ok), object : IOrderDialogListener {
            override fun onDialogClick(text: String, dialog: IOrderDialog) {
                hideDialog()
            }
        }).show()
    }

    fun setPositiveButton(text: String, listener: IOrderDialogListener): IOrderDialog {
        mView.button_positive.text = text
        this.positiveListener = listener
        return this
    }

    fun setNegativeButton(text: String, listener: IOrderDialogListener): IOrderDialog {
        mView.button_negative.visibility = View.VISIBLE
        mView.button_negative.text = text
        this.negativeListener = listener
        return this
    }

    override fun show() {
        super.show()
        mView.layout_dialog.startAnimation(AnimationUtils.loadAnimation(mView.context, R.anim.dialog_scale_in))
    }

    fun hideDialog() {
        val animation = AnimationUtils.loadAnimation(mView.context, R.anim.dialog_scale_out)
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                dismiss()
                isHide = false
            }

        })
        mView.layout_dialog.startAnimation(animation)
    }

    override fun setCancelable(cancelable: Boolean) {
        this.isCancel = cancelable
    }

    override fun onBackPressed() {
        if (!isHide){
            hideDialog()
            isHide = true
        }
    }

    interface IOrderDialogListener {
        fun onDialogClick(text: String, dialog: IOrderDialog)
    }
}