package com.example.stepupandroid.model.response

class OrderDetailResponse(
    val result: Result
)

data class Result(
    val id: Int,
    val service_id: Int,
    val order_by: Int,
    val cancel_by: Int,
    val isCancel: Boolean,
    val isAgreementAgreed: String,
    val cancel_at: String,
    val cancel_desc: String,
    val order_title: String,
    val order_description: String,
    val order_status: Int,
    val order_attachments: Map<String, String>,
    val expected_expand_date: String,
    val expand_end_date: String,
    val expected_start_date: String,
    val expected_end_date: String,
    val accepted_at: String,
    val created_by: Int,
    val updated_by: Int,
    val created_at: String,
    val updated_at: String,
    val start_at: String?,
    val service_order: String,
    val freelancer_id: Int,
    val completed_attachments: Map<String, String>,
    val stringStatus: String,
    val service: Service,
    val contact: Contact
)

data class Service(
    val title: String,
    val description: String,
    val price: String,
    val requirement: String,
    val discount: Float
)

data class Contact(
    val name: String,
    val email: String
)