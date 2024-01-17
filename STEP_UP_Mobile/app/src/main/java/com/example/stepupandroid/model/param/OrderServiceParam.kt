package com.example.stepupandroid.model.param

import android.os.Parcelable
import com.example.stepupandroid.model.Attachment
import kotlinx.parcelize.Parcelize

@Parcelize
class OrderServiceParam(
    val service_id: String,
    val order_title: String,
    val order_description: String,
    val attachments: List<Attachment>,
    var expected_start_date: String,
    var expected_end_date: String,
    val isAgreementAgreed: String
) : Parcelable