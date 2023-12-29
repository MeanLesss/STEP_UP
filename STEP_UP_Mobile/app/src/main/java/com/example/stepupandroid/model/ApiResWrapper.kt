package com.example.stepupandroid.model

import com.google.gson.JsonObject
import java.io.Serializable

data class ApiResWrapper<T>(
    val code: Int? = null,
    val message: String = "",
    val success: Boolean = false,
    val errors: JsonObject? = null,
    val data: T? = null
) : Serializable