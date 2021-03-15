package com.oss.digihealth.nur.ui.nursedesk.nurse_desk_vitals.model.response

data class NurseDeskVitalsResponseModel(
    val responseContents: List<NurseDeskVitalsresponseContent?>? = listOf(),
    val message: String? = "",
    val statusCode: Int? = 0,
    val totalRecords: Int? = 0
)