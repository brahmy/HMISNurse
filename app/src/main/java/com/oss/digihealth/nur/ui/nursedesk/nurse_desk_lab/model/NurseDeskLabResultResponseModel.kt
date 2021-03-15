package com.oss.digihealth.nur.ui.nursedesk.nurse_desk_lab.model

data class NurseDeskLabResultResponseModel(
    val msg: String = "",
    val req: Req = Req(),
    val responseContents: List<ResponseContentX> = listOf(),
    val statusCode: Int = 0
)