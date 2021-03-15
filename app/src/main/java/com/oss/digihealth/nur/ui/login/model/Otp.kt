package com.oss.digihealth.nur.ui.login.model

data class Otp(
    val created_by: Int? = 0,
    val created_date: String? = "",
    val fps_id: Any? = Any(),
    val mobile1: Any? = Any(),
    val modified_by: Int? = 0,
    val modified_date: String? = "",
    val name: String? = "",
    val otp: String? = "",
    val otp_expiry_time: String? = "",
    val revision: Int? = 0,
    val status: Boolean? = false,
    val user_uuid: Int? = 0,
    val uuid: Int? = 0
)