package com.oss.digihealth.nur.ui.nursedesk.nurse_desk_radiology.model

data class NurseDeskRadiologyResulyResponseModel(
    val message: String = "",
    val responseContents: List<NUrseDeskRadiologyResultResponseContent> = listOf(),
    val statusCode: Int = 0
)