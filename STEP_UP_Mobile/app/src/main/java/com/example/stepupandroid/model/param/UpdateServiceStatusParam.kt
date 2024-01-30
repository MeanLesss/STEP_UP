package com.example.stepupandroid.model.param

data class UpdateServiceStatusParam(
    val service_id: String,
    val is_active: Boolean,
    val start_date: String,
    val end_date: String
)
