package com.example.stepupandroid.model.param

import android.os.Parcelable
import com.example.stepupandroid.model.Attachment
import kotlinx.parcelize.Parcelize

@Parcelize
class CreateServiceParam(
    val title: String,
    val description: String,
    val attachments: List<Attachment>,
    val price: String,
    val service_type: String,
    var start_date: String,
    var end_date: String
) : Parcelable