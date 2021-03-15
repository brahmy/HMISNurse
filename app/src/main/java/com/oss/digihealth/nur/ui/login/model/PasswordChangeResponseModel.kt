package com.oss.digihealth.nur.ui.login.model

data class PasswordChangeResponseModel(
    val msg: String? = "",
    val req: Req? = Req(),
    val responseContents: List<Int?>? = listOf(),
    val statusCode: Int? = 0
)