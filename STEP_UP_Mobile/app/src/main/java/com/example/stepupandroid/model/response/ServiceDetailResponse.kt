package com.example.stepupandroid.model.response

class ServiceDetailResponse(
    val result: ServiceDetail
)

data class ServiceDetail(
    val id: Int,
    val title: String,
    val description: String,
    val status: Int,
    val attachments: Map<String, String>,
    val requirement: String,
    val price: Int,
    val discount: Int,
    val service_type: String,
    val start_date: String,
    val end_date: String,
    val created_by: String,
    val updated_by: String,
    val created_at: String,
    val updated_at: String,
    val view: Int,
    val service_rate: Int,
    val service_ordered_count: Int,
    val isReadOnly: Boolean,
    val stringStatus: String
)