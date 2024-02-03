package com.example.stepupandroid.model.response

data class MyServiceResponse(
    val result: List<MyServiceItem>,
)

data class MyServiceItem(
    val id: Int,
    val title: String,
    val description: String,
    val status: Int,
    val attachments: Map<String, String>, // Using a map to represent dynamic keys
    val requirement: String?, // Assuming it's nullable
    val price: String,
    val discount: Float,
    val service_type: String,
    val start_date: String,
    val end_date: String,
    val created_by: Int,
    val updated_by: Int,
    val created_at: String,
    val updated_at: String,
    val view: Int,
    val service_rate: Int,
    val service_ordered_count: Int,
    val stringStatus: String,
)