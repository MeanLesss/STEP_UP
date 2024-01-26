package com.example.stepupandroid.model.response

data class ServiceResponse(
    val result : ServiceWrapper
)

data class ServiceWrapper(
    val current_page: Int,
    val data: List<ServiceItem>,
    val first_page_url: String,
    val from: Int,
    val last_page: Int,
    val last_page_url: String,
    val links: List<Link>,
    val next_page_url: String?,
    val path: String,
    val per_page: Int,
    val prev_page_url: String?,
    val to: Int,
    val total: Int
)

data class ServiceItem(
    val id: Int,
    val title: String,
    val description: String,
    val status: Int,
    val attachments: Map<String, String>?,
    val requirement: Any?, // You may want to replace "Any?" with a specific type
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
    val service_ordered_count: Int
)

data class Link(
    val url: String?,
    val label: String,
    val active: Boolean
)
