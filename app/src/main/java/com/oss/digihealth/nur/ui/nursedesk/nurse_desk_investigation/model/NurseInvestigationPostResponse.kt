package com.oss.digihealth.nur.ui.nursedesk.nurse_desk_investigation.model

data class NurseInvestigationPostResponse(
    val msg: String = "",
    val req: Req = Req(),
    val responseContents: ResponseContents = ResponseContents(),
    val statusCode: Int = 0
)