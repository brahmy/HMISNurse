package com.oss.digihealth.nur.utils.custom_views

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.animation.AnimationUtils
import com.oss.digihealth.nur.R
import kotlinx.android.synthetic.main.dialog_progress.*


class CustomProgressDialog(context: Context?) : Dialog(context!!) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_progress)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
    }

    override fun dismiss() {
        super.dismiss()
        progressGifImageView.clearAnimation()
    }

    override fun show() {
        super.show()
        val rotationAnimation = AnimationUtils.loadAnimation(context, R.anim.progress_anim)
        rotationAnimation.isFillEnabled = true
        progressGifImageView.startAnimation(rotationAnimation)
    }

}