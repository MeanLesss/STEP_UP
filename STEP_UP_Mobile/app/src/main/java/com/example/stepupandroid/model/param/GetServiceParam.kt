package com.example.stepupandroid.model.param

data class GetServiceParam(
    val service_type: String = "",
    val price: String = "",
    val title: String = "",
    val range: Int = 0,
    val page: Int = 0,
)
