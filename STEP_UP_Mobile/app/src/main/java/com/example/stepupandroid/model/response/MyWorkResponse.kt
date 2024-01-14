package com.example.stepupandroid.model.response

data class MyWorkResponse(
    val result: List<MyWork>
)

data class MyWork(
    val id: Int,
    val service_id: Int,
    val order_by: Int,
    val cancel_by: Int,
    val isCancel: Boolean,
    val isAgreementAgreed: Int,
    val cancel_at: String?,
    val cancel_desc: String,
    val order_title: String,
    val order_description: String,
    val order_status: Int,
//    val order_attachments: String, //Map<String, String>?,
    val expected_expand_date: String,
    val expand_end_date: String,
    val expected_start_date: String,
    val expected_end_date: String,
    val accepted_at: String,
    val created_by: String,
    val updated_by: String,
    val created_at: String,
    val updated_at: String,
    val start_at: String,
    val service_order: String,
    val freelancer_id: Int,
    val completed_attachments: String, //Map<String, String>?,
    val status: String
)