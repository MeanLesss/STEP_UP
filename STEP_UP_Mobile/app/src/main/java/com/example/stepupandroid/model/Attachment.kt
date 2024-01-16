package com.example.stepupandroid.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Attachment(val fileName: String, val fileUri: Uri) : Parcelable
