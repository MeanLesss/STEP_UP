package com.example.stepupandroid.model.response

class OrderSummaryResponse (
    val service_id: Int,
    val order_title: String,
    val order_description: String,
    val expected_start_date: String,
    val expected_end_date: String,
    val isAgreementAgreed: String,
    val order_attachments: List<String>,
    val tax: String,
    val price: String,
    val totalPrice: String
)