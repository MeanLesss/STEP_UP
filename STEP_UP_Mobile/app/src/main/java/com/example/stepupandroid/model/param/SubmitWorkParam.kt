package com.example.stepupandroid.model.param

import com.example.stepupandroid.model.Attachment

class SubmitWorkParam(
    val order_id: String,
    val service_id: String,
    val attachments: List<Attachment>
)