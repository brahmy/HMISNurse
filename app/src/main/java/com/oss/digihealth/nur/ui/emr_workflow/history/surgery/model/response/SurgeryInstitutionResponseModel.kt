package com.oss.digihealth.nur.ui.emr_workflow.history.surgery.model.response

data class SurgeryInstitutionResponseModel(
    val responseContents: List<InstitutionresponseContent?>? = listOf(),
    val msg: String? = "",
    val statusCode: Int? = 0,
    val totalRecords: Int? = 0
)