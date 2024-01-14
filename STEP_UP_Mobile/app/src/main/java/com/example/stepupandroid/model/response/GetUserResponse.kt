package com.example.stepupandroid.model.response

class GetUserResponse(
    val user_info: UserInfo,
    val user_detail: UserDetail
)

data class UserInfo(
    val id: Int,
    val name: String,
    val email: String,
    val isGuest: Boolean,
    val role: Int,
    val email_verified_at: String,
    val login_attempt: String,
    val created_at: String,
    val updated_at: String
)

data class UserDetail(
    val id: Int,
    val user_id: Int,
    val phone: String,
    val id_card_no: String,
    val job_type: String,
    val id_attachment: String,
    val profile_image: String,
    val card_number: String,
    val card_name: String,
    val card_cvv: String,
    val card_date: String,
    val credit_score: String,
    val balance: String,
    val created_by: String,
    val updated_by: String,
    val created_at: String,
    val updated_at: String
)