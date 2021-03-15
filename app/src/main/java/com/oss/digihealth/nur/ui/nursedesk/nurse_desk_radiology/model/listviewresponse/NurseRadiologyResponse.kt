package com.oss.digihealth.nur.ui.nursedesk.nurse_desk_radiology.model.listviewresponse

data class NurseRadiologyResponse(
    var message: String? = "",
    var responseContents: List<ResponseContent?>? = listOf(),
    var statusCode: Int? = 0
)