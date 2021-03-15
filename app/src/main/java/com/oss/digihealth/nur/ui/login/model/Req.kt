package com.oss.digihealth.nur.ui.login.model

data class Req(
    val otp: String? = "",
    val password: String? = "",
    val username: String? = ""
)