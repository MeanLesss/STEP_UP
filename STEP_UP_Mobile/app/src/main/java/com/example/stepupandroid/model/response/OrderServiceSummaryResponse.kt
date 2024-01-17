package com.example.stepupandroid.model.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class OrderServiceSummaryResponse(
    val result: OrderSummary
) : Parcelable

@Parcelize
data class OrderSummary(
    val service_id: String,
    val isAgreementAgreed: String,
    val tax: String,
    val price: String,
    val totalPrice: String
): Parcelable