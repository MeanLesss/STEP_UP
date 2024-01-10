package com.example.stepupandroid.model.param

import java.io.File
import java.io.Serializable

class OrderParam(
    val service_id: Int,
    val order_title: String,
    val order_description: String,
    val attachment_files: List<File>,
    val expected_start_date: String,
    val expected_end_date: String,
    val isAgreementAgreed: String
): Serializable