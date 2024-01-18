package com.example.stepupandroid.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar

class LoadingDialog(
    context: Context
) : Dialog(context) {

    init {
        setCancelable(false)
        setCanceledOnTouchOutside(false)
    }

    override fun show() {
        try {
            if (!isShowing) {
                super.show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun hide() {
        try {
            if (isShowing) {
                super.dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val progressBar = ProgressBar(context)
        val param: ViewGroup.LayoutParams = ViewGroup.LayoutParams(150,150);
        progressBar.layoutParams = param
        progressBar.setPadding(30,30,30,30)

        val view = FrameLayout(context)
        view.addView(progressBar)

        setContentView(view)
    }
}