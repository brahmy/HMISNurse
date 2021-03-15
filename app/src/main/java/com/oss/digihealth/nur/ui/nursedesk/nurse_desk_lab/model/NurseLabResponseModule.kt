package com.oss.digihealth.nur.ui.nursedesk.nurse_desk_lab.model

data class NurseLabResponseModule(
    var message: String? = "",
    var responseContents: List<ResponseContent?>? = listOf(),
    var statusCode: Int? = 0
)