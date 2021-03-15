package com.oss.digihealth.nur.ui.nursedesk.nurse_desk_investigation.model

data class NurseDeskInvestigationResponseModel(
    val message: String = "",
    val responseContents: List<NurseDeskInvestigationResponseContent> = listOf(),
    val statusCode: Int = 0
)