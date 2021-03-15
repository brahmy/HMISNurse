package com.oss.digihealth.nur.ui.emr_workflow.prescription.model

data class PrescriptionDurationResponseModel(
    val message: String = "",
    val responseContents: List<PrescriptionDurationResponseContent> = listOf(),
    val statusCode: Int = 0,
    val totalRecords: Int = 0
)