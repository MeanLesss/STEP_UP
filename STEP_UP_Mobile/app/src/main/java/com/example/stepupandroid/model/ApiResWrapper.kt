package com.example.stepupandroid.model

import com.google.gson.JsonObject
import java.io.Serializable

data class ApiResWrapper<T>(
    val code: Int? = null,
    val msg: String = "",
    val status: String = "",
    val errors: JsonObject? = null,
    val data: T? = null
) : Serializable