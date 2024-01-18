package com.example.stepupandroid.base

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.stepupandroid.ui.dialog.LoadingDialog

open class BaseViewModel(context: Context): ViewModel() {
    var loadingDialog = LoadingDialog(context)
}
